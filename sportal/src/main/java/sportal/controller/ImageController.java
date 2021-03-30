package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sportal.exceptions.AuthenticationException;
import sportal.exceptions.NotFoundException;
import sportal.model.dto.ImageToArticleResponseDTO;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleImage;
import sportal.model.pojo.User;
import sportal.model.repository.IImageRepository;
import sportal.model.repository.IArticleRepository;
import sportal.service.ImageService;
import sportal.util.SessionManager;
import sportal.util.Validator;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.util.Optional;

@RestController
public class ImageController extends AbstractController{

    @Autowired
    ImageService imageService;
    @Autowired
    IArticleRepository articleRepository;
    @Autowired
    IImageRepository articleImageRepository;
    @Autowired
    SessionManager sessionManager;
    @Autowired
    private UserController userController;

    @Value("${file.path}")
    private String filePath;

    @PostMapping("/article/{id}/images")
    public ImageToArticleResponseDTO addImageToArticle(@PathVariable int id, @RequestPart MultipartFile file, HttpSession ses){
        if (isAdmin(sessionManager.getLoggedUser(ses))){
            throw new AuthenticationException("Requires admin privileges!");
        } else {
            Optional<Article> a = articleRepository.findById(id);
            if (!a.isPresent()) {
                throw new NotFoundException("Trying to upload image to not existing article");
            }
            Article article = a.get();
            File pFile = new File(filePath + File.separator + id + "_" + System.nanoTime() + ".png");
            try (OutputStream os = new FileOutputStream(pFile)) {
                os.write(file.getBytes());
                os.close();
                return imageService.addImageToArticle(pFile, article);
            } catch (FileNotFoundException e) {
                throw new NotFoundException("No files to upload");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }
    }

    @GetMapping(value = "/images/{id}", produces = "image/*")
    public byte[] download(@PathVariable int id) throws IOException {
        ArticleImage articleImage = articleImageRepository.findById(id).get();
        String url = articleImage.getUrl();
        File pFile = new File(url);
        return Files.readAllBytes(pFile.toPath());
    }

    public boolean isAdmin(User user){
        return userController.userIsAdmin(user);
    }
}
