package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.model.dto.ImageToArticleResponseDTO;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleImage;
import sportal.model.repository.IArticleRepository;
import sportal.model.repository.IImageRepository;

import java.io.File;
import sportal.util.OptionalResultVerifier;

@Service
public class ImageService {

    @Autowired
    private OptionalResultVerifier orv;
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

    private void checkIfParentArticleIsPosted(int id) {
        Runnable thread = () -> {
            try {
                Thread.sleep(60000);
                ArticleImage image = orv.verifyOptionalResult(imageRepository.findById(id));
                if(image.getArticle() == null){
                    imageRepository.delete(image);
                    File f = new File(image.getUrl());
                    f.delete();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(thread).start();
    }
}
