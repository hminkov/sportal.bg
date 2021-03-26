package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.model.dto.AddCommentRequestDTO;
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

    public void addComment(User loggedUser, AddCommentRequestDTO addedComment) {
        Comment comment = new Comment();
        comment.setUser(loggedUser);
        comment.setArticle(orv.verifyOptionalResult(articleRepository.findById(addedComment.getArticleId())));
        comment.setCommentText(addedComment.getText());
        comment.setPostDate(LocalDateTime.now());
        commentRepository.save(comment);
    }
}
