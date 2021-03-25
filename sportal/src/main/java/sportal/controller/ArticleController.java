package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sportal.model.dto.CreateArticleRequestDTO;
import sportal.model.dto.CreateArticleResponseDTO;
import sportal.service.ArticleService;

@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @PostMapping("/articles/new")
    public CreateArticleResponseDTO createNewArticle(@RequestBody CreateArticleRequestDTO article){
        //TODO validate admin priviliges
        return articleService.postNewArticle(article);
    }
}
