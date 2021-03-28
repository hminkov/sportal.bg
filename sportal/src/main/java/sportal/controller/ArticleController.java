package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.model.dto.ArticleHeadingResponseDTO;
import sportal.model.dto.CreateArticleRequestDTO;
import sportal.model.dto.ArticleResponseDTO;
import sportal.model.dto.EditArticleRequestDTO;
import sportal.model.pojo.User;
import sportal.model.repository.ICategoryRepository;
import sportal.model.repository.IUserRepository;
import sportal.service.ArticleService;

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

    @PostMapping("/articles/new")
    public ArticleResponseDTO createNewArticle(@RequestBody CreateArticleRequestDTO article){
        //TODO validate admin privileges
        return articleService.postNewArticle(article);
    }

    @GetMapping("/articles/{id}")
    public ArticleResponseDTO viewArticle(@PathVariable int id){
        return articleService.getArticleById(id);
    }

    @GetMapping("/users/{username}/article")
    public List<ArticleResponseDTO> getArticleByAuthor(@PathVariable String username){
        return articleService.getArticleByAuthor(iUserRepository.findByUsername(username));
    }


    @GetMapping("/articles/top5")
    public List<ArticleResponseDTO> topFiveMostViewedArticles(){
        return articleService.getTopFiveMostViewed();
    }

//    @GetMapping("/articles/{category}")
//    public List<ArticleResponseDTO> getArticleByCategory(@PathVariable String category){
//        return articleService.getArticleByCategory(iCategoryRepository.findArticleCategoryByName(category));
//    }

    @GetMapping("/articles")
    public List<ArticleHeadingResponseDTO> getAllArticleTitles(){
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
