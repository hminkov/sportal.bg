package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import sportal.exceptions.AuthenticationException;
import sportal.model.dto.*;
import sportal.model.pojo.User;
import sportal.service.ArticleService;
import sportal.service.SessionService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@RestController
public class ArticleController extends AbstractController{

    @Autowired
    ArticleService articleService;
    @Autowired
    SessionService sessionManager;
    @Autowired
    private UserController userController;

    @PostMapping("/articles")
    public ArticleResponseDTO createNewArticle(@RequestBody ArticleCreateRequestDTO article, HttpSession ses){
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

    @PutMapping("/articles/authors")
    public List<ArticleSearchByAuthorResponseDTO> getArticlesByAuthor(@RequestBody ArticleSeаrchByAuthorRequestDTO pagesDTO){
        return articleService.getArticlesByAuthor(pagesDTO);
    }

    @PutMapping("/articles/title")
    public List<ArticleHeadingResponseDTO> getArticleByHeading(@RequestBody ArticleHeadingSearchRequestDTO articleRequest){
        return articleService.getArticleByName(articleRequest);
    }

    @PutMapping("/categories/{id}")
    public List<ArticleResponseWithoutComDTO> getArticleByCategory(@PathVariable int id,@RequestBody PagedSearchRequestDTO pagesDTO){
        return articleService.articleByCategory(id, pagesDTO.getPage(), pagesDTO.getResultsPerPage());
    }

    @GetMapping("/articles/latest")
    public List<ArticleResponseWithoutComDTO> latestArticles(){
        return articleService.latestArticles();
    }

    @GetMapping("/articles/top5")
    public List<ArticleResponseWithoutComDTO> topFiveMostViewedArticles(){
        return articleService.getTopFiveMostViewed();
    }

    @PutMapping("/articles/all")
    public List<ArticleHeadingResponseDTO> getAllArticles(@RequestBody PagedSearchRequestDTO pagesDTO){
        return articleService.getAllArticles(pagesDTO);
    }

    @PutMapping("/articles")
    public ArticleResponseDTO editArticle(@RequestBody ArticleEditRequestDTO article, HttpSession ses){
        if(isAdmin(sessionManager.getLoggedUser(ses))){
            return articleService.editArticle(article, article.getId());
        }
        else{
            throw new AuthenticationException("Requires admin privileges");
        }
    }

    @DeleteMapping("/articles")
    public DeleteEntityResponseDTO deleteArticle(@RequestBody DeleteEntityRequestDTO article, HttpSession ses){
        if(isAdmin(sessionManager.getLoggedUser(ses))){
            return new DeleteEntityResponseDTO(articleService.deleteArticle(article.getId()));
        }
        else{
            throw new AuthenticationException("Requires admin privileges");
        }
    }

    @PutMapping("/articles/{articleId}/like")
    public ArticleResponseDTO likeArticle(@PathVariable int articleId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        return new ArticleResponseDTO(articleService.likeArticle(loggedUser.getId(), articleId));
    }

    @PutMapping("/articles/{articleId}/dislike")
    public ArticleResponseDTO dislikeArticle(@PathVariable int articleId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        return new ArticleResponseDTO(articleService.dislikeArticle(loggedUser.getId(), articleId));
    }

    @PutMapping("/articles/{articleId}/unlike")
    public ArticleResponseDTO unlikeArticle(@PathVariable int articleId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        return new ArticleResponseDTO(articleService.unlikeArticle(loggedUser.getId(), articleId));
    }

    @PutMapping("/articles/{articleId}/undislike")
    public ArticleResponseDTO undislikeArticle(@PathVariable int articleId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        return new ArticleResponseDTO(articleService.undislikeArticle(loggedUser.getId(), articleId));
    }
    public boolean isAdmin(User user){
        return userController.userIsAdmin(user);
    }
}
