package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exceptions.NotFoundException;
import sportal.model.dao.ArticleDAO;
import sportal.model.dto.*;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleCategory;
import sportal.model.pojo.User;
import sportal.model.repository.IArticleRepository;
import sportal.model.repository.ICategoryRepository;
import sportal.model.repository.IUserRepository;
import sportal.util.OptionalResultVerifier;
import sportal.util.Validator;

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
    private ArticleDAO articleDAO;
    @Autowired
    private OptionalResultVerifier orv;

    public ArticleResponseDTO postNewArticle(CreateArticleRequestDTO requestArticle) {
        Validator.validateText(requestArticle.getText());
        Validator.validateText(requestArticle.getHeading());
        Article realArticle = new Article();
        realArticle.setHeading(requestArticle.getHeading());
        realArticle.setArticleText((requestArticle.getText()));
        User user = orv.verifyOptionalResult(userRepository.findById(requestArticle.getCreatorId()));
        realArticle.setAuthor(user);
        realArticle.setCategory(createNewCategoryOrReturnMatching(requestArticle.getCategory()));
        realArticle.setPostDate(LocalDateTime.now());
        articleRepository.save(realArticle);
        return new ArticleResponseDTO(realArticle);
    }

    public synchronized ArticleResponseDTO getArticleById(int id) {
        Article article = orv.verifyOptionalResult(articleRepository.findById(id));
        article.setViews(article.getViews()+1);
        articleRepository.save(article);
        return new ArticleResponseDTO(orv.verifyOptionalResult(articleRepository.findById(id)));
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
        return new ArticleResponseDTO(orv.verifyOptionalResult(articleRepository.findById(ogArticle.getId())));
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

    public void likeArticle(int userId, int articleId) {
        articleDAO.likeArticle(userId, articleId);
    }

    public void dislikeArticle(int userId, int articleId){
        articleDAO.dislikeArticle(userId, articleId);
    }

    public void unlikeArticle(int userId, int articleId) {
        articleDAO.unlikeArticle(userId, articleId);
    }

    public void undislikeArticle(int userId, int articleId) {
        articleDAO.undislikeArticle(userId, articleId);
    }

    public List<ArticleHeadingDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleHeadingDTO> articleByHeadingDTO = new ArrayList<>();
        for(Article a : articles){
            articleByHeadingDTO.add(new ArticleHeadingDTO(a));
        }
        return articleByHeadingDTO;
    }

    public List<ArticleResponseDTO> getArticleByAuthor(UserWithoutPasswordResponseDTO authorRequest) {
        User u = userRepository.findByUsername(authorRequest.getUsername());
        if(u != null) {
            List<Article> articles = articleRepository.findAll();
            List<ArticleResponseDTO> articleResponseDTO = new ArrayList<>();
            boolean isFound = false;
            for (Article a : articles) {
                if (a.getAuthor().getId() == u.getId()) {
                    articleResponseDTO.add(new ArticleResponseDTO(a));
                    isFound = true;
                }
            }
            if(isFound) {
                return articleResponseDTO;
            }
            throw new NotFoundException("No articles found from this author");
        }
        throw new NotFoundException("Author not found");
    }
    public List<ArticleResponseDTO> getArticleByName(String articleName){
        List<Article> articles = articleDAO.getArticleByHeading(articleName);
        if(articles.size() > 0) {
            List<ArticleResponseDTO> articleResponseDTO = new ArrayList<>();
            for (Article a : articles) {
                Optional<Article> articleById = articleRepository.findById(a.getId());
                articleById.ifPresent(article -> articleResponseDTO.add(new ArticleResponseDTO(article)));
            }
            return articleResponseDTO;
        }
        throw new NotFoundException("Article with such title not found");
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

    public ArticleCategoryDTO articleByCategory(int catID) {
        Optional<ArticleCategory> categoryOptional = categoryRepository.findById(catID);
        if(categoryOptional.isPresent()){
            return new ArticleCategoryDTO(categoryOptional.get());
        }
        else{
            throw new NotFoundException("Category not found");
        }
    }
}
