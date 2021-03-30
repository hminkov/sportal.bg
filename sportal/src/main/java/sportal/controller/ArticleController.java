package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import sportal.exceptions.AuthenticationException;
import sportal.model.dto.*;
import sportal.model.pojo.User;
import sportal.service.ArticleService;
import sportal.util.SessionManager;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@RestController
public class ArticleController extends AbstractController{

    @Autowired
    ArticleService articleService;
    @Autowired
    SessionManager sessionManager;
    @Autowired
    private UserController userController;

    @PostMapping("/articles")
    public ArticleResponseDTO createNewArticle(@RequestBody CreateArticleRequestDTO article, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        if(isAdmin(loggedUser)){
            return articleService.postNewArticle(article, loggedUser);
        }
        else{
            throw new AuthenticationException("Requires admin privileges");
        }
    }

    @GetMapping("/articles/{id}")
    public ArticleResponseDTO viewArticle(@PathVariable int id){
        return articleService.getArticleById(id);
    }

    @GetMapping("/articles/authors")
    public List<ArticleResponseDTO> getArticleByAuthor(@RequestBody UserIDResponseDTO author){
        return articleService.getArticleByAuthor(author);
    }

    @GetMapping("/articles/by-name")
    public List<ArticleResponseDTO> getArticleByHeading(@RequestBody ArticleHeadingDTO articleName){
        return articleService.getArticleByName(articleName.getHeading());
    }

    @GetMapping("/categories/{id}")
    public ArticleCategoryDTO getArticleByCategory(@PathVariable int id){
        return articleService.articleByCategory(id);
    }

    @GetMapping("/articles/top5")
    public List<ArticleResponseDTO> topFiveMostViewedArticles(){
        return articleService.getTopFiveMostViewed();
    }

    @GetMapping("/articles")
    public List<ArticleHeadingDTO> getAllArticleTitles(){
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
