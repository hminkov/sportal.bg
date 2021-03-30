package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exceptions.AuthenticationException;
import sportal.model.dto.*;
import sportal.model.pojo.User;
import sportal.model.repository.ICategoryRepository;
import sportal.model.repository.IUserRepository;
import sportal.service.ArticleService;
import sportal.util.SessionManager;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ArticleController extends AbstractController{

    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    ICategoryRepository iCategoryRepository;
    @Autowired
    ArticleService articleService;
    @Autowired
    SessionManager sessionManager;
    @Autowired
    private UserController userController;

    @PostMapping("/articles")
    public ArticleResponseDTO createNewArticle(@RequestBody CreateArticleRequestDTO article, HttpSession ses){
        if(isAdmin(sessionManager.getLoggedUser(ses))){
            return articleService.postNewArticle(article);
        }
        else{
            throw new AuthenticationException("Requires admin privileges");
        }
    }

    @GetMapping("/articles/{id}")
    public ArticleResponseDTO viewArticle(@PathVariable int id){
        return articleService.getArticleById(id);
    }

    @GetMapping("/users/{username}/articles")
    public List<ArticleResponseDTO> getArticlesByAuthor(@PathVariable String username){
        return articleService.getArticleByAuthor(iUserRepository.findByUsername(username));
    }

    @GetMapping("/articles/top5")
    public List<ArticleResponseDTO> topFiveMostViewedArticles(){
        return articleService.getTopFiveMostViewed();
    }

    @GetMapping("/articles/{category}")
    public ArticleCategoryDTO getArticlesByCategory(@PathVariable String category){
        return articleService.articleByCategory(category);
    }

    @GetMapping("/articles")
    public List<ArticleHeadingResponseDTO> getAllArticleTitles(){
        return articleService.getAllArticles();
    }

    @PutMapping("/articles")
    public ArticleResponseDTO editArticle(@RequestBody EditArticleRequestDTO article, HttpSession ses){
        if(isAdmin(sessionManager.getLoggedUser(ses))){
            return articleService.editArticle(article, article.getArticleId());
        }
        else{
            throw new AuthenticationException("Requires admin privileges");
        }
    }

    @DeleteMapping("/articles/")
    public void deleteArticle(@RequestBody DeleteArticleRequestDTO article, HttpSession ses){
        if(isAdmin(sessionManager.getLoggedUser(ses))){
            articleService.deleteArticle(article.getArticleId());
        }
        else{
            throw new AuthenticationException("Requires admin privileges");
        }
    }

    @PutMapping("/articles/{articleId}/like")
    public void likeArticle(@PathVariable int articleId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        articleService.likeArticle(loggedUser.getId(), articleId);
    }

    @PutMapping("/articles/{articleId}/dislike")
    public void dislikeArticle(@PathVariable int articleId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        articleService.dislikeArticle(loggedUser.getId(), articleId);
    }

    @PutMapping("/articles/{articleId}/unlike")
    public void unlikeArticle(@PathVariable int articleId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        articleService.unlikeArticle(loggedUser.getId(), articleId);
    }

    @PutMapping("/articles/{articleId}/undislike")
    public void undislikeArticle(@PathVariable int articleId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        articleService.undislikeArticle(loggedUser.getId(), articleId);
    }

    private boolean isAdmin(User user){
        return userController.userIsAdmin(user);
    }
}
