package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sportal.exceptions.BadRequestException;
import sportal.model.dto.CreateArticleRequestDTO;
import sportal.model.dto.CreateArticleResponseDTO;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleCategory;
import sportal.model.pojo.User;
import sportal.model.repository.IArticleRepository;
import sportal.model.repository.ICategoryRepository;
import sportal.model.repository.IUserRepository;
import sportal.util.OptionalResultVerifier;

import java.time.LocalDateTime;

@Service
@Component
public class ArticleService {

    @Autowired
    IArticleRepository articleRepository;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    ICategoryRepository categoryRepository;
    @Autowired
    OptionalResultVerifier orv;

    public CreateArticleResponseDTO postNewArticle(CreateArticleRequestDTO requestArticle) {
        if(requestArticle.getHeading().isEmpty() || requestArticle.getText().isEmpty()){
            throw new BadRequestException("Articles must have a heading and a body");
        }
        Article realArticle = new Article();
        realArticle.setHeading(requestArticle.getHeading());
        realArticle.setArticleText((requestArticle.getText()));
        User user = orv.verifyOptionalResult(userRepository.findById(requestArticle.getCreatorId()));
        realArticle.setAuthor(user);
        ArticleCategory category = new ArticleCategory();
        category.setCategoryName(requestArticle.getCategory());
        realArticle.setCategory(category);
        categoryRepository.save(category);
        realArticle.setPostDate(LocalDateTime.now());
        articleRepository.save(realArticle);
        return new CreateArticleResponseDTO(realArticle);
    }
}
