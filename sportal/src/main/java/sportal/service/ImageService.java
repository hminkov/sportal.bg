package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sportal.model.pojo.ArticleImage;
import sportal.model.repository.IArticleRepository;
import sportal.model.repository.IImageRepository;

import java.io.File;
import sportal.exceptions.NotFoundException;

import sportal.util.OptionalResultVerifier;

import java.io.*;

@Service
public class ImageService {

    @Autowired
    private OptionalResultVerifier orv;
    @Autowired
    IArticleRepository iArticleRepository;
    @Autowired
    IImageRepository imageRepository;

//    public ImageToArticleResponseDTO addImageToArticle(File pFile, Article article) {
//        ArticleImage articleImage = new ArticleImage();
//        articleImage.setUrl(pFile.getAbsolutePath());
//        articleImage.setArticle(article);
//        imageRepository.save(articleImage);
//        System.out.println("1 - " + articleImage.getUrl());
//        System.out.println("2 - " + articleImage.getArticle().getArticleText());
//        System.out.println("3 - " + article.getHeading());
//        return new ImageToArticleResponseDTO(articleImage);
//    }


    public ArticleImage uploadImage(String filePath, int articleId, MultipartFile file){
        File pFile = new File(filePath + File.separator + articleId + "_" + System.nanoTime() + ".png");
        try(OutputStream os = new FileOutputStream(pFile)){
            os.write(file.getBytes());
            ArticleImage articleImage = new ArticleImage();
            articleImage.setUrl(pFile.getAbsolutePath());
            articleImage =  imageRepository.save(articleImage);
            checkIfParentArticleIsPosted(orv.verifyOptionalResult(imageRepository.findById(articleImage.getId())).getId());
            return articleImage;
        }catch (FileNotFoundException e){
            throw new NotFoundException("No files to upload" + e.getMessage());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return null;
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
