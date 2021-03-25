package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sportal.exceptions.NotFoundException;
import sportal.model.pojo.ArticleImage;
import sportal.model.repository.IArticleImageRepository;
import sportal.model.repository.IArticleRepository;

import java.io.*;
import java.nio.file.Files;

@RestController
public class ImageController {

    @Autowired
    IArticleRepository articleRepository;
    @Autowired
    IArticleImageRepository articleImageRepository;

    @Value("${file.path}")
    private String filePath;

    @PutMapping("images/upload")
    public ArticleImage upload(@PathVariable int id, @RequestPart MultipartFile file){
        //create physical file -> java.io.File
        File pFile = new File(filePath + File.separator + id + "_" + System.nanoTime() + ".png");
        //write all bytes from the multipartfile
        try(OutputStream os = new FileOutputStream(pFile)){
            os.write(file.getBytes());
            //create an articleImage object
            ArticleImage articleImage = new ArticleImage();
            //set its url to the path of the physical file
            articleImage.setUrl(pFile.getAbsolutePath());
            articleImage.setArticle(articleRepository.findById(id).get());
            //save articleImage object
            articleImageRepository.save(articleImage);
            //return articleImage object
            return articleImage;
        }catch (FileNotFoundException e){
            throw new NotFoundException("No files to upload");
        }catch (IOException e){

        }
        return null;
    }

    @GetMapping(value = "/images/{id}", produces = "image/*")
    public byte[] download(@PathVariable int id) throws IOException {
        //find the articleImage object from db, extract its url
        ArticleImage articleImage = articleImageRepository.findById(id).get();
        //get the physical file from the url
        String url = articleImage.getUrl();
        File pFile = new File(url);
        //read its bytes and write them into response body
        return Files.readAllBytes(pFile.toPath());
    }
}
