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
import sportal.service.ImageService;
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
    private ImageService imageService;
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
        return imageService.uploadImage(filePath, id, file);
    }

    @GetMapping(value = "/images/{id}", produces = "image/*")
    public byte[] download(@PathVariable int id) throws IOException {
        ArticleImage articleImage = orv.verifyOptionalResult(articleImageRepository.findById(id));
        String url = articleImage.getImgUrl();
        File pFile = new File(url);
        return Files.readAllBytes(pFile.toPath());
    }
}
