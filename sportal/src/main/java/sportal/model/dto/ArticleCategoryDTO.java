package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleCategory;


import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Component
public class ArticleCategoryDTO {
    private String category_name;
    private List<Article> articles;

    public ArticleCategoryDTO(ArticleCategory a) {
        category_name = a.getName();
        articles = a.getArticles();
    }
}
