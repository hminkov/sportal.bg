package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sportal.exceptions.AuthenticationException;
import sportal.model.dto.DeleteEntityRequestDTO;
import sportal.model.dto.DeleteEntityResponseDTO;
import sportal.model.dto.ImageUploadResponseDTO;
import sportal.model.pojo.ArticleImage;
import sportal.model.pojo.User;
import sportal.model.repository.IImageRepository;
import sportal.service.ImageService;
import sportal.service.SessionService;
import sportal.util.OptionalResultVerifier;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;

@RestController
public class ImageController extends AbstractController{

    @Autowired
    private ImageService imageService;
    @Autowired
    private IImageRepository imageRepository;
    @Autowired
    private SessionService sessionManager;
    @Autowired
    private UserController userController;
    @Autowired
    private OptionalResultVerifier orv;

    @Value("${file.path}")
    private String filePath;

    @PostMapping("/images")
    public ImageUploadResponseDTO addImageToArticle(@RequestPart MultipartFile file, HttpSession ses) {
        User loggedUser = sessionManager.getLoggedUser(ses);
        if (!userController.userIsAdmin(loggedUser)) {
            throw new AuthenticationException("Requires admin privileges!");
        }
        return new ImageUploadResponseDTO(imageService.uploadImage(filePath, file));
    }

    @GetMapping(value = "/images/{id}", produces = "image/*")
    public byte[] download(@PathVariable int id) throws IOException {
        ArticleImage articleImage = orv.verifyOptionalResult(imageRepository.findById(id));
        String url = articleImage.getUrl();
        File pFile = new File(url);
        return Files.readAllBytes(pFile.toPath());
    }

    @DeleteMapping("/images")
    public DeleteEntityResponseDTO deleteImage(@RequestBody DeleteEntityRequestDTO deleteRq, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        if(userController.userIsAdmin(loggedUser)){
            return new DeleteEntityResponseDTO(imageService.deleteImage(deleteRq.getId()));
        }
        else{
            throw new AuthenticationException("Deleting images requires admin privileges");
        }
    }
}
