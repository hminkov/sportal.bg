package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.POJO;


@NoArgsConstructor
@Getter
@Setter
public class DeleteEntityResponseDTO {
    private int id;

    public DeleteEntityResponseDTO(int id) {
        this.id = id;
    }
}
