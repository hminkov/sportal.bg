package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sportal.model.dto.AddCommentRequestDTO;
import sportal.model.dto.ArticleResponseDTO;
import sportal.model.pojo.User;
import sportal.service.ArticleService;
import sportal.service.CommentService;

import javax.servlet.http.HttpSession;

@RestController
public class CommentController {

    @Autowired
    SessionManager sessionManager;
    @Autowired
    ArticleService articleService;
    @Autowired
    CommentService commentService;

    @PostMapping("/articles/{articleId}/comments")
    public ArticleResponseDTO postComment(HttpSession ses, @RequestBody AddCommentRequestDTO comment){
        User loggedUser = sessionManager.getLoggedUser(ses);
        commentService.addComment(loggedUser, comment);
        return articleService.getArticle(comment.getArticleId());
    }
}
