package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Setter
@Getter
@Component
public class AddCommentReplyRequestDTO {

    private int parentCommentId;
    private int articleId;
    private String text;
}
