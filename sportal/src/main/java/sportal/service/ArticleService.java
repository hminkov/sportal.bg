package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sportal.exceptions.BadRequestException;
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
import sportal.util.OptionalResultVerifier;
import sportal.util.Validator;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private IArticleRepository articleRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IImageRepository imageRepository;
    @Autowired
    private ArticleDAO articleDAO;
    @Autowired
    private OptionalResultVerifier orv;

    public ArticleResponseDTO postNewArticle(ArticleCreateRequestDTO requestArticle, User author) {
        Validator.validateText(requestArticle.getText());
        Validator.validateText(requestArticle.getHeading());
        verifyThatImagesAreNotTaken(requestArticle);
        Article realArticle = new Article();
        realArticle.setHeading(requestArticle.getHeading());
        realArticle.setText(requestArticle.getText());
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

    public ArticleResponseDTO editArticle(ArticleEditRequestDTO editedArticle, int articleId) {
        Validator.validateText(editedArticle.getText());
        Validator.validateText(editedArticle.getHeading());
        Article ogArticle = orv.verifyOptionalResult(articleRepository.findById(articleId));
        ogArticle.setHeading(editedArticle.getHeading());
        ogArticle.setText(editedArticle.getText());
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
        categoryName = categoryName.toLowerCase();
        if(categoryRepository.findByName(categoryName).isEmpty()){
            ArticleCategory newCategory = new ArticleCategory();
            newCategory.setName(categoryName);
            categoryRepository.save(newCategory);
        }
        return orv.verifyOptionalResult(categoryRepository.findByName(categoryName));
    }

    public int deleteArticle(int articleId) {
        Article article = orv.verifyOptionalResult(articleRepository.findById(articleId));
        articleRepository.delete(article);
        return article.getId();
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
        Validator.validatePaging(pageRequest.getPage(),pageRequest.getResultsPerPage());
        Page<Article> articles = articleRepository.findAll(PageRequest.of(pageRequest.getPage(), pageRequest.getResultsPerPage()));
        List<ArticleHeadingResponseDTO> articleByHeadingDTO = new ArrayList<>();
        for(Article a : articles){
            articleByHeadingDTO.add(new ArticleHeadingResponseDTO(a));
        }
        return articleByHeadingDTO;
    }

    public List<ArticleHeadingResponseDTO> getArticlesByAuthor(ArticleSeаrchByAuthorRequestDTO pageRequest) {
        Validator.validatePaging(pageRequest.getPage(),pageRequest.getResultsPerPage());
        List<ArticleHeadingResponseDTO> articleResponse = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getResultsPerPage());
        Page<Article> articles = articleRepository.findArticlesByAuthor_UsernameContaining(pageRequest.getUsername(), pageable);
        for(Article a : articles){
            articleResponse.add(new ArticleHeadingResponseDTO(a));
        }
        return articleResponse;
    }

    public List<ArticleHeadingResponseDTO> getArticleByName(ArticleHeadingSearchRequestDTO articleSearchRequest){
        Validator.validatePaging(articleSearchRequest.getPage(),articleSearchRequest.getResultsPerPage());
        Pageable pageable = PageRequest.of(articleSearchRequest.getPage(), articleSearchRequest.getResultsPerPage());
        List<Article> articles = articleDAO.getArticleByHeading(articleSearchRequest.getHeading(), pageable);
        List<ArticleHeadingResponseDTO> articleResults = new ArrayList<>();
        for(Article a : articles){
            articleResults.add(new ArticleHeadingResponseDTO(a));
        }
        return articleResults;
    }
    public List<ArticleResponseWithoutComDTO>latestArticles(){
        List<Article> articlesByDate = articleRepository.findByOrderByPostDateDesc()
                .stream()
                .limit(5)
                .collect(Collectors.toList());
        List<ArticleResponseWithoutComDTO> latestArticles = new ArrayList<>();
        for(Article a : articlesByDate){
            latestArticles.add(new ArticleResponseWithoutComDTO(a));
        }
        return latestArticles;
    }
    public List<ArticleResponseWithoutComDTO> getTopFiveMostViewed() {
        List<Article> articles = articleDAO.topFiveMostViewedArticles();
        List<ArticleResponseWithoutComDTO> articleResponseWithoutComDTO = new ArrayList<>();
        if(articles.size() > 0) {
            for (Article a : articles) {
                Optional<Article> articleById = articleRepository.findById(a.getId());
                articleById.ifPresent(article -> articleResponseWithoutComDTO.add(new ArticleResponseWithoutComDTO(article)));
            }
            return articleResponseWithoutComDTO;
        }
        return articleResponseWithoutComDTO;
    }

    public List<ArticleResponseWithoutComDTO> articleByCategory(int catID, int page, int resultsPerPage) {
        Optional<ArticleCategory> categoryOptional = categoryRepository.findById(catID);
        if(categoryOptional.isPresent()){
            return articleDAO.articlesByCategoryId(catID,page,resultsPerPage);
        }
        else{
            throw new NotFoundException("Category not found");
        }
    }

    private void verifyThatImagesAreNotTaken(ArticleCreateRequestDTO requestArticle) {
        for(int imageId : requestArticle.getImageIds()){
            ArticleImage image = orv.verifyOptionalResult(imageRepository.findById(imageId));
            if(image.getArticle() != null){
                throw new BadRequestException("One of the images you've chosen for your article already belongs to another article.");
            }
        }
    }
}
