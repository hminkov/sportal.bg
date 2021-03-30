package sportal.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleCategory;


import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Component
public class ArticleCategoryDTO {
    private String category_name;
    @JsonIgnore
    private List<Article> articles;
    private List<ArticleResponseDTO> articleResponseDTOS;

    public ArticleCategoryDTO(ArticleCategory a) {
        category_name = a.getName();
        articles = a.getArticles();
        articleResponseDTOS = new ArrayList<>();
        for(Article article : articles){
            articleResponseDTOS.add(new ArticleResponseDTO(article));
        }
    }
}
