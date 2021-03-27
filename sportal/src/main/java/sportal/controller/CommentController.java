package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.model.dto.AddCommentRequestDTO;
import sportal.model.dto.ArticleResponseDTO;
import sportal.model.pojo.User;
import sportal.service.ArticleService;
import sportal.service.CommentService;

import javax.servlet.http.HttpSession;

@RestController
public class CommentController extends AbstractController{

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

    @PutMapping("/comments/{commentId}/like")
    public void likeComment(HttpSession ses, @PathVariable int commentId){
        User loggedUser = sessionManager.getLoggedUser(ses);
        commentService.likeComment(loggedUser.getId(), commentId);
    }

    @PutMapping("/comments/{commentId}/dislike")
    public void dislikeComment(HttpSession ses, @PathVariable int commentId){
        User loggedUser = sessionManager.getLoggedUser(ses);
        commentService.dislikeComment(loggedUser.getId(), commentId);
    }
}
