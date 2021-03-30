package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sportal.exceptions.AuthenticationException;
import sportal.exceptions.NotFoundException;
import sportal.model.pojo.ArticleImage;
import sportal.model.pojo.User;
import sportal.model.repository.IArticleImageRepository;
import sportal.model.repository.IArticleRepository;
import sportal.model.repository.IUserRepository;
import sportal.util.OptionalResultVerifier;
import sportal.util.SessionManager;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.util.Optional;

@RestController
public class ImageController extends AbstractController{

    @Autowired
    IArticleRepository articleRepository;
    @Autowired
    IArticleImageRepository articleImageRepository;
    @Autowired
    SessionManager sessionManager;
    @Autowired
    UserController userController;
    @Autowired
    private OptionalResultVerifier orv;

    @Value("${file.path}")
    private String filePath;

    @PostMapping("/articles/{id}/images")
    public ArticleImage upload(@PathVariable int id, @RequestPart MultipartFile file, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        if(!userController.userIsAdmin(loggedUser)){
            throw new AuthenticationException("You need admin privileges to upload images");
        }
        //create physical file -> java.io.File
        File pFile = new File(filePath + File.separator + id + "_" + System.nanoTime() + ".png");
        //write all bytes from the multipartfile
        try(OutputStream os = new FileOutputStream(pFile)){
            os.write(file.getBytes());
            //create an articleImage object
            ArticleImage articleImage = new ArticleImage();
            //set its url to the path of the physical file
            articleImage.setImgUrl(pFile.getAbsolutePath());
            //save articleImage object
            articleImage =  articleImageRepository.save(articleImage);
            //return articleImage object
            checkIfParentArticleIsPosted(orv.verifyOptionalResult(articleImageRepository.findById(articleImage.getId())).getId());
            return articleImage;
        }catch (FileNotFoundException e){
            throw new NotFoundException("No files to upload");
        }catch (IOException e){

        }
        return null;
    }

    private void checkIfParentArticleIsPosted(int id) {
        Runnable thread = () -> {
            try {
                Thread.sleep(60000);
                ArticleImage image = orv.verifyOptionalResult(articleImageRepository.findById(id));
                if(image.getArticle() == null){
                    articleImageRepository.delete(image);
                    File f = new File(image.getImgUrl());
                    f.delete();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(thread).start();
    }

    @GetMapping(value = "/images/{id}", produces = "image/*")
    public byte[] download(@PathVariable int id) throws IOException {
        //find the articleImage object from db, extract its url
        ArticleImage articleImage = articleImageRepository.findById(id).get();
        //get the physical file from the url
        String url = articleImage.getImgUrl();
        File pFile = new File(url);
        //read its bytes and write them into response body
        return Files.readAllBytes(pFile.toPath());
    }
}
