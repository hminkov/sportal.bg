package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exceptions.BadRequestException;
import sportal.model.dto.CreateArticleRequestDTO;
import sportal.model.dto.CreateArticleResponseDTO;
import sportal.model.pojo.Article;
import sportal.model.pojo.User;
import sportal.model.repository.IArticleRepository;
import sportal.model.repository.IUserRepository;
import sportal.util.OptionalResultVerifier;

@Service
public class ArticleService {

    @Autowired
    IArticleRepository articleRepository;
    @Autowired
    IUserRepository userRepository;
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
        articleRepository.save(realArticle);
        return new CreateArticleResponseDTO(realArticle);
    }
}
