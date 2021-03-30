package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.model.dao.CommentDAO;
import sportal.model.dto.AddCommentRequestDTO;
import sportal.model.dto.ArticleResponseDTO;
import sportal.model.dto.EditCommentRequestDTO;
import sportal.model.dto.addCommentReplyRequestDTO;
import sportal.model.pojo.Article;
import sportal.model.pojo.Comment;
import sportal.model.pojo.User;
import sportal.model.repository.IArticleRepository;
import sportal.model.repository.ICommentRepository;
import sportal.util.OptionalResultVerifier;
import sportal.util.Validator;

import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    ICommentRepository commentRepository;
    @Autowired
    IArticleRepository articleRepository;
    @Autowired
    OptionalResultVerifier orv;
    @Autowired
    CommentDAO commentDAO;

    public ArticleResponseDTO addComment(User loggedUser, AddCommentRequestDTO addedComment) {
        Validator.validateText(addedComment.getText());
        Article article = orv.verifyOptionalResult(articleRepository.findById(addedComment.getArticleId()));
        Comment comment = new Comment(addedComment.getText(), LocalDateTime.now(), article, loggedUser);
        commentRepository.save(comment);
        return new ArticleResponseDTO(article);
    }

    public void likeComment(int commentId, int userId) {
        commentDAO.likeComment(commentId, userId);
    }

    public void dislikeComment(int commentId, int userId){
        commentDAO.dislikeComment(commentId, userId);
    }

    public void unlikeComment(int userId, int commentId) {
        commentDAO.unlikeComment(userId, commentId);
    }

    public void undislikeComment(int userId, int commentId) {
        commentDAO.undislikeComment(userId, commentId);
    }

    public boolean userOwnsComment(int userId, int commentId) {
        Comment comment = orv.verifyOptionalResult(commentRepository.findById(commentId));
        return comment.getUser().getId() == userId;
    }

    public ArticleResponseDTO deleteComment(int commentId) {
        Comment comment = orv.verifyOptionalResult(commentRepository.findById(commentId));
        comment.setCommentText("deleted on " + LocalDateTime.now());
        return new ArticleResponseDTO(orv.verifyOptionalResult(articleRepository.findById(comment.getArticle().getId())));
    }

    public ArticleResponseDTO editComment(EditCommentRequestDTO editedComment) {
        Validator.validateText(editedComment.getText());
        Comment comment = orv.verifyOptionalResult(commentRepository.findById(editedComment.getId()));
        comment.setCommentText(editedComment.getText());
        commentRepository.save(comment);
        return new ArticleResponseDTO(orv.verifyOptionalResult(articleRepository.findById(editedComment.getArticleId())));
    }

    public ArticleResponseDTO addCommentReply(User loggedUser, addCommentReplyRequestDTO reply) {
        Validator.validateText(reply.getText());
        Article article = orv.verifyOptionalResult(articleRepository.findById(reply.getArticleId()));
        Comment parent = orv.verifyOptionalResult(commentRepository.findById(reply.getParentCommentId()));
        Comment comment = new Comment(reply.getText(), LocalDateTime.now(), article, loggedUser, parent);
        commentRepository.save(comment);
        return new ArticleResponseDTO(article);
    }
}
