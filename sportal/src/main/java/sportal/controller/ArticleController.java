package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.model.dto.CreateArticleRequestDTO;
import sportal.model.dto.ArticleResponseDTO;
import sportal.model.dto.EditArticleRequestDTO;
import sportal.service.ArticleService;

@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @PostMapping("/articles/new")
    public ArticleResponseDTO createNewArticle(@RequestBody CreateArticleRequestDTO article){
        //TODO validate admin priviliges
        return articleService.postNewArticle(article);
    }

    @GetMapping("/articles/{id}")
    public ArticleResponseDTO viewArticle(@PathVariable int id){
        return articleService.getArticle(id);
    }

    @PutMapping("/articles/{userId}")
    public ArticleResponseDTO editArticle(@PathVariable int userId, @RequestBody EditArticleRequestDTO article){
        //TODO validate admin priviliges
        return articleService.editArticle(article);
    }

    @DeleteMapping("/articles/{userId}/{articleId}")
    public void deleteArticle(@PathVariable int userId, @PathVariable int articleId){
        //TODO validate admin priviliges
        articleService.deleteArticle(articleId);
    }
}
