package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.model.dao.CommentDAO;
import sportal.model.dto.CommentAddReplyRequestDTO;
import sportal.model.dto.CommentAddRequestDTO;
import sportal.model.dto.ArticleResponseDTO;
import sportal.model.dto.CommentEditRequestDTO;
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

    public ArticleResponseDTO addComment(User loggedUser, CommentAddRequestDTO addedComment) {
        Article article = orv.verifyOptionalResult(articleRepository.findById(addedComment.getArticleId()));
        Comment comment = new Comment(addedComment.getText(), LocalDateTime.now(), article, loggedUser);
        commentRepository.save(comment);
        return new ArticleResponseDTO(article);
    }

    public Article likeComment(int userId, int commentId) {
        Comment comment = orv.verifyOptionalResult(commentRepository.findById(commentId));
        commentDAO.likeComment(userId, commentId);
        return orv.verifyOptionalResult(articleRepository.findById(comment.getArticle().getId()));
    }

    public Article dislikeComment(int userId, int commentId){
        Comment comment = orv.verifyOptionalResult(commentRepository.findById(commentId));
        commentDAO.dislikeComment(userId, commentId);
        return orv.verifyOptionalResult(articleRepository.findById(comment.getArticle().getId()));
    }

    public Article unlikeComment(int userId, int commentId) {
        Comment comment = orv.verifyOptionalResult(commentRepository.findById(commentId));
        commentDAO.unlikeComment(userId, commentId);
        return orv.verifyOptionalResult(articleRepository.findById(comment.getArticle().getId()));
    }

    public Article undislikeComment(int userId, int commentId) {
        Comment comment = orv.verifyOptionalResult(commentRepository.findById(commentId));
        commentDAO.undislikeComment(userId, commentId);
        return orv.verifyOptionalResult(articleRepository.findById(comment.getArticle().getId()));
    }

    public boolean userOwnsComment(int userId, int commentId) {
        Comment comment = orv.verifyOptionalResult(commentRepository.findById(commentId));
        return comment.getUser().getId() == userId;
    }

    public int deleteComment(int commentId) {
        Comment comment = orv.verifyOptionalResult(commentRepository.findById(commentId));
        commentRepository.delete(comment);
        return commentId;
    }

    public ArticleResponseDTO editComment(CommentEditRequestDTO editedComment) {
        Comment comment = orv.verifyOptionalResult(commentRepository.findById(editedComment.getId()));
        Validator.validateText(editedComment.getText());
        comment.setCommentText(editedComment.getText());
        commentRepository.save(comment);
        int articleId =  comment.getArticle().getId();
        return new ArticleResponseDTO(orv.verifyOptionalResult(articleRepository.findById(articleId)));
    }

    public ArticleResponseDTO addCommentReply(User loggedUser, CommentAddReplyRequestDTO reply) {
        Comment parent = orv.verifyOptionalResult(commentRepository.findById(reply.getParentCommentId()));
        Article article = parent.getArticle();
        Comment comment = new Comment(reply.getText(), LocalDateTime.now(), article, loggedUser, parent);
        commentRepository.save(comment);
        return new ArticleResponseDTO(article);
    }
}
