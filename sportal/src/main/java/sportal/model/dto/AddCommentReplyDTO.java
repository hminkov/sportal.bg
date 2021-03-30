package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@Component
public class AddCommentReplyDTO {
    private int parentCommentId;
    private int articleId;
    private String text;
}
