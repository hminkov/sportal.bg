package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.model.dto.ArticleByHeadingDTO;
import sportal.model.dto.CreateArticleRequestDTO;
import sportal.model.dto.ArticleResponseDTO;
import sportal.model.dto.EditArticleRequestDTO;
import sportal.model.pojo.User;
import sportal.service.ArticleService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ArticleController extends AbstractController{

    @Autowired
    ArticleService articleService;
    @Autowired
    SessionManager sessionManager;

    @PostMapping("/articles/new")
    public ArticleResponseDTO createNewArticle(@RequestBody CreateArticleRequestDTO article){
        //TODO validate admin privileges
        return articleService.postNewArticle(article);
    }

    @GetMapping("/articles/{id}")
    public ArticleResponseDTO viewArticle(@PathVariable int id){
        return articleService.getArticleById(id);
    }

//    @GetMapping("/articles/author/{author_name}")
//    public ArticleResponseDTO viewArticleByAuthor(@PathVariable String author_name){
//        return articleService.getArticleByAuthor(author_name);
//    }

    @GetMapping("/articles")
    public List<ArticleByHeadingDTO> getAllArticleTitles(){
        return articleService.getAllArticles();
    }

    @PutMapping("/articles/{articleId}")
    public ArticleResponseDTO editArticle(@PathVariable int articleId, @RequestBody EditArticleRequestDTO article){
        //TODO validate admin privileges
        return articleService.editArticle(article, articleId);
    }

    @DeleteMapping("/articles/{articleId}")
    public void deleteArticle(@PathVariable int articleId){
        //TODO validate admin privileges
        articleService.deleteArticle(articleId);
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
}
