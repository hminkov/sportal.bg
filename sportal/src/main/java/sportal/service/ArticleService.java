package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sportal.exceptions.NotFoundException;
import sportal.model.dao.ArticleDAO;
import sportal.model.dto.*;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleCategory;
import sportal.model.pojo.ArticleImage;
import sportal.model.pojo.User;
import sportal.model.repository.IArticleRepository;
import sportal.model.repository.ICategoryRepository;
import sportal.model.repository.IImageRepository;
import sportal.model.repository.IUserRepository;
import sportal.util.OptionalResultVerifier;
import sportal.util.Validator;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private IArticleRepository articleRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IImageRepository imageRepository;
    @Autowired
    private ArticleDAO articleDAO;
    @Autowired
    private OptionalResultVerifier orv;

    public ArticleResponseDTO postNewArticle(CreateArticleRequestDTO requestArticle, User author) {
        Validator.validateText(requestArticle.getText());
        Validator.validateText(requestArticle.getHeading());
        Article realArticle = new Article();
        realArticle.setHeading(requestArticle.getHeading());
        realArticle.setArticleText((requestArticle.getText()));
        realArticle.setAuthor(author);
        realArticle.setCategory(createNewCategoryOrReturnMatching(requestArticle.getCategory()));
        realArticle.setPostDate(LocalDateTime.now());
        realArticle = saveArticleToDb(realArticle, requestArticle.getImageIds());
        return new ArticleResponseDTO(realArticle);
    }

    @Transactional
    Article saveArticleToDb(Article realArticle, int[] imageIds) {
        articleRepository.save(realArticle);
        for(int id : imageIds){
            ArticleImage image = orv.verifyOptionalResult(imageRepository.findById(id));
            image.setArticle(realArticle);
            realArticle.getImages().add(image);
            imageRepository.save(image);
        }
        return realArticle;
    }

    public synchronized ArticleResponseDTO getArticleById(int id) {
        Article article = orv.verifyOptionalResult(articleRepository.findById(id));
        article.setViews(article.getViews()+1);
        articleRepository.save(article);
        return new ArticleResponseDTO(article);
    }

    public ArticleResponseDTO editArticle(EditArticleRequestDTO editedArticle, int articleId) {
        Validator.validateText(editedArticle.getText());
        Article ogArticle = orv.verifyOptionalResult(articleRepository.findById(articleId));
        ogArticle.setHeading(editedArticle.getHeading());
        ogArticle.setArticleText(editedArticle.getText());
        String editedCategoryName = editedArticle.getCategory();
        if(!ogArticle.getCategory().getName().equals(editedCategoryName)){
            ogArticle.setCategory(createNewCategoryOrReturnMatching(editedCategoryName));
        }
        else {
            ogArticle.setCategory(orv.verifyOptionalResult(categoryRepository.findByName(editedCategoryName)));
        }
        articleRepository.save(ogArticle);
        return new ArticleResponseDTO(ogArticle);
    }

    private ArticleCategory createNewCategoryOrReturnMatching(String categoryName){
        Validator.validateText(categoryName);
        if(categoryRepository.findByName(categoryName).isEmpty()){
            ArticleCategory newCategory = new ArticleCategory();
            newCategory.setName(categoryName);
            categoryRepository.save(newCategory);
        }
        return orv.verifyOptionalResult(categoryRepository.findByName(categoryName));
    }

    public void deleteArticle(int articleId) {
        Article article = orv.verifyOptionalResult(articleRepository.findById(articleId));
        articleRepository.delete(article);
    }

    public Article likeArticle(int userId, int articleId) {
        orv.verifyOptionalResult(articleRepository.findById(articleId));
        articleDAO.likeArticle(userId, articleId);
        return orv.verifyOptionalResult(articleRepository.findById(articleId));
    }

    public Article dislikeArticle(int userId, int articleId){
        orv.verifyOptionalResult(articleRepository.findById(articleId));
        articleDAO.dislikeArticle(userId, articleId);
        return orv.verifyOptionalResult(articleRepository.findById(articleId));
    }

    public Article unlikeArticle(int userId, int articleId) {
        orv.verifyOptionalResult(articleRepository.findById(articleId));
        articleDAO.unlikeArticle(userId, articleId);
        return orv.verifyOptionalResult(articleRepository.findById(articleId));
    }

    public Article undislikeArticle(int userId, int articleId) {
        orv.verifyOptionalResult(articleRepository.findById(articleId));
        articleDAO.undislikeArticle(userId, articleId);
        return orv.verifyOptionalResult(articleRepository.findById(articleId));
    }

    public List<ArticleHeadingResponseDTO> getAllArticles(PagedSearchRequestDTO pageRequest) {
        Page<Article> articles = articleRepository.findAll(PageRequest.of(pageRequest.getPage(), pageRequest.getResultsPerPage()));
        List<ArticleHeadingResponseDTO> articleByHeadingDTO = new ArrayList<>();
        for(Article a : articles){
            articleByHeadingDTO.add(new ArticleHeadingResponseDTO(a));
        }
        return articleByHeadingDTO;
    }

    public List<ArticleHeadingResponseDTO> getArticlesByAuthor(String username, int page, int resultsPerPage) {
        User user = orv.verifyOptionalResult(userRepository.findByUsername(username));
        Pageable pageable = PageRequest.of(page, resultsPerPage);
        Page<Article> articles = articleRepository.findArticlesByAuthor(user, pageable);
        List<ArticleHeadingResponseDTO> articleResponse = new ArrayList<>();
        for(Article a : articles){
            articleResponse.add(new ArticleHeadingResponseDTO(a));
        }
        return articleResponse;
    }

    public List<ArticleHeadingResponseDTO> getArticleByName(ArticleHeadingSearchRequestDTO articleSearchRequest){
        Pageable pageable = PageRequest.of(articleSearchRequest.getPage(), articleSearchRequest.getResultsPerPage());
        List<Article> articles = articleDAO.getArticleByHeading(articleSearchRequest.getHeading(), pageable);
        List<ArticleHeadingResponseDTO> articleResults = new ArrayList<>();
        for(Article a : articles){
            articleResults.add(new ArticleHeadingResponseDTO(a));
        }
        return articleResults;
    }

    public List<ArticleResponseWithoutComDTO> getTopFiveMostViewed() {
        List<Article> articles = articleDAO.topFiveMostViewedArticles();
        if(articles.size() > 0) {
            List<ArticleResponseWithoutComDTO> articleResponseWithoutComDTO = new ArrayList<>();
            for (Article a : articles) {
                Optional<Article> articleById = articleRepository.findById(a.getId());
                articleById.ifPresent(article -> articleResponseWithoutComDTO.add(new ArticleResponseWithoutComDTO(article)));
            }
            return articleResponseWithoutComDTO;
        }
        throw new NotFoundException("No articles found");
    }

    public ArticleCategoryDTO articleByCategory(int catID, int page, int resultsPerPage) {
        Optional<ArticleCategory> categoryOptional = categoryRepository.findById(catID);
        if(categoryOptional.isPresent()){
            return new ArticleCategoryDTO(categoryOptional.get(), page, resultsPerPage);
        }
        else{
            throw new NotFoundException("Category not found");
        }
    }
}
