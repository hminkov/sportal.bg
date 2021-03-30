package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.model.dto.ImageToArticleResponseDTO;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleImage;
import sportal.model.repository.IArticleRepository;
import sportal.model.repository.IImageRepository;

import java.io.File;

@Service
public class ImageService {

    @Autowired
    IArticleRepository iArticleRepository;
    @Autowired
    IImageRepository imageRepository;

    public ImageToArticleResponseDTO addImageToArticle(File pFile, Article article) {
        ArticleImage articleImage = new ArticleImage();
        articleImage.setUrl(pFile.getAbsolutePath());
        articleImage.setArticle(article);
        imageRepository.save(articleImage);
        System.out.println("1 - " + articleImage.getUrl());
        System.out.println("2 - " + articleImage.getArticle().getArticleText());
        System.out.println("3 - " + article.getHeading());
        return new ImageToArticleResponseDTO(articleImage);
    }
}
