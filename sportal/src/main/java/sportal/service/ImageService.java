package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sportal.exceptions.NotFoundException;
import sportal.model.pojo.ArticleImage;
import sportal.model.pojo.User;
import sportal.model.repository.IImageRepository;
import sportal.util.OptionalResultVerifier;

import java.io.*;

@Service
public class ImageService {

    @Autowired
    private IImageRepository imageRepository;
    @Autowired
    private OptionalResultVerifier orv;


    public ArticleImage uploadImage(String filePath, MultipartFile file){
        File pFile = new File(filePath + File.separator + "_" + System.nanoTime() + ".png");
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

    public int deleteImage(int imageId) {
        ArticleImage image = orv.verifyOptionalResult(imageRepository.findById(imageId));
        imageRepository.delete(image);
        File f = new File(image.getUrl());
        f.delete();
        return image.getId();
    }
}