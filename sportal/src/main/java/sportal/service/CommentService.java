package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.model.dao.CommentDAO;
import sportal.model.dto.AddCommentRequestDTO;
import sportal.model.dto.ArticleResponseDTO;
import sportal.model.pojo.Comment;
import sportal.model.pojo.User;
import sportal.model.repository.IArticleRepository;
import sportal.model.repository.ICommentRepository;
import sportal.util.OptionalResultVerifier;

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

    public void addComment(User loggedUser, AddCommentRequestDTO addedComment) {
        Comment comment = new Comment();
        comment.setUser(loggedUser);
        comment.setArticle(orv.verifyOptionalResult(articleRepository.findById(addedComment.getArticleId())));
        comment.setCommentText(addedComment.getText());
        comment.setPostDate(LocalDateTime.now());
        commentRepository.save(comment);
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

//    public ArticleResponseDTO deleteComment(int commentId) {
//        Comment comment
//    }
}
