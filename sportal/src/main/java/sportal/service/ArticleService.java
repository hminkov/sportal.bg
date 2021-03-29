package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sportal.exceptions.BadRequestException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Component
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
        if(requestArticle.getHeading().isEmpty() || requestArticle.getText().isEmpty()){
            throw new BadRequestException("Articles must have a heading and a body");
        }
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

    public List<ArticleHeadingResponseDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleHeadingResponseDTO> articleByHeadingDTO = new ArrayList<>();
        for(Article a : articles){
            articleByHeadingDTO.add(new ArticleHeadingResponseDTO(a));
        }
        return articleByHeadingDTO;
    }

    public List<ArticleResponseDTO> getArticleByAuthor(User authorId) {
        List<Article> articles = articleRepository.findAll();
        List<ArticleResponseDTO> articleResponseDTO = new ArrayList<>();
        for(Article a : articles){
            if(a.getAuthor().getId() == authorId.getId()) {
                articleResponseDTO.add(new ArticleResponseDTO(a));
            }
        }
        return articleResponseDTO;
    }

    public List<ArticleResponseDTO> getTopFiveMostViewed() {
        List<ArticleResponseDTO> articleResponseDTOS = new ArrayList<>();
        List<Article> articles = articleDAO.topFiveMostViewedArticles();
        for(Article a : articles){
            articleResponseDTOS.add(new ArticleResponseDTO(a));
        }
        return articleResponseDTOS;
    }

    public ArticleCategoryDTO articleByCategory(String category) {
        Optional<ArticleCategory> categoryOptional = categoryRepository.findByName(category);
        if(categoryOptional.isPresent()){
            return new ArticleCategoryDTO(categoryOptional.get());
        }
        else{
            throw new NotFoundException("Category not found");
        }
    }
}
