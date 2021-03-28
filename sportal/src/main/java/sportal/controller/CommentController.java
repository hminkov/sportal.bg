package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exceptions.BadRequestException;
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
    @Autowired
    UserController userController;

    @PostMapping("/articles/{articleId}/comments")
    public ArticleResponseDTO postComment(HttpSession ses, @RequestBody AddCommentRequestDTO comment){
        User loggedUser = sessionManager.getLoggedUser(ses);
        commentService.addComment(loggedUser, comment);
        return articleService.getArticleById(comment.getArticleId());
    }

//    @DeleteMapping("/comments{commentId}")
//    public ArticleResponseDTO deleteComment(HttpSession ses, @PathVariable int commentId){
//        User loggedUser = sessionManager.getLoggedUser(ses);
//        if(userController.userIsAdmin(loggedUser) || userOwnsComment(loggedUser.getId(), commentId)){
//            return commentService.deleteComment(commentId);
//        }
//        else{
//            throw new BadRequestException("You can only delete your own comments");
//        }
//    }


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

    @PutMapping("/comments/{commentId}/unlike")
    public void unlikeComment(@PathVariable int commentId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        commentService.unlikeComment(loggedUser.getId(), commentId);
    }

    @PutMapping("/comments/{commentId}/undislike")
    public void undislikeComment(@PathVariable int commentId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        commentService.undislikeComment(loggedUser.getId(), commentId);
    }

    private boolean userOwnsComment(int userId, int commentId) {
        return commentService.userOwnsComment(userId, commentId);
    }
}
