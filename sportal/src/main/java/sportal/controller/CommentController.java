package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exceptions.BadRequestException;
import sportal.model.dto.CommentAddReplyRequestDTO;
import sportal.model.dto.ArticleResponseDTO;
import sportal.model.dto.*;
import sportal.model.pojo.User;
import sportal.service.ArticleService;
import sportal.service.CommentService;
import sportal.util.SessionManager;

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

    @PostMapping("comments")
    public ArticleResponseDTO postComment(HttpSession ses, @RequestBody CommentAddRequestDTO comment){
        User loggedUser = sessionManager.getLoggedUser(ses);
        return commentService.addComment(loggedUser, comment);
    }

    @PostMapping("comments/replies")
    public ArticleResponseDTO replyToComment(HttpSession ses, @RequestBody CommentAddReplyRequestDTO reply){
        User loggedUser = sessionManager.getLoggedUser(ses);
        return commentService.addCommentReply(loggedUser, reply);
    }

    @DeleteMapping("/comments")
    public ArticleResponseDTO deleteComment(HttpSession ses, @RequestBody DeleteEntityRequestDTO comment){
        User loggedUser = sessionManager.getLoggedUser(ses);
        if(userController.userIsAdmin(loggedUser) || userOwnsComment(loggedUser.getId(), comment.getId())){
            return commentService.deleteComment(comment.getId());
        }
        else{
            throw new BadRequestException("You can only delete your own comments");
        }
    }

    @PutMapping("/comments")
    public ArticleResponseDTO editComment(HttpSession ses, @RequestBody CommentEditRequestDTO comment){
        User loggedUser = sessionManager.getLoggedUser(ses);
        if(commentService.userOwnsComment(loggedUser.getId(), comment.getId())){
            return commentService.editComment(comment);
        }
        else{
            throw new BadRequestException("Only the owner of the comment can edit it");
        }
    }

    @PutMapping("/comments/{commentId}/like")
    public ArticleResponseDTO likeComment(HttpSession ses, @PathVariable int commentId){
        User loggedUser = sessionManager.getLoggedUser(ses);
        return new ArticleResponseDTO(commentService.likeComment(loggedUser.getId(), commentId));
    }

    @PutMapping("/comments/{commentId}/dislike")
    public ArticleResponseDTO dislikeComment(HttpSession ses, @PathVariable int commentId){
        User loggedUser = sessionManager.getLoggedUser(ses);
        return new ArticleResponseDTO(commentService.dislikeComment(loggedUser.getId(), commentId));
    }

    @PutMapping("/comments/{commentId}/unlike")
    public ArticleResponseDTO unlikeComment(@PathVariable int commentId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        return new ArticleResponseDTO(commentService.unlikeComment(loggedUser.getId(), commentId));
    }

    @PutMapping("/comments/{commentId}/undislike")
    public ArticleResponseDTO undislikeComment(@PathVariable int commentId, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        return new ArticleResponseDTO(commentService.undislikeComment(loggedUser.getId(), commentId));
    }

    private boolean userOwnsComment(int userId, int commentId) {
        return commentService.userOwnsComment(userId, commentId);
    }
}
