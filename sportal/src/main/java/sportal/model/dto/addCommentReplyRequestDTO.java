package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class addCommentReplyRequestDTO {

    private int parentCommentId;
    private int articleId;
    private String text;
}
