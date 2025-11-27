package aem.java.strayfinder.web.rest;

import aem.java.strayfinder.errors.StrayNotFoundException;
import aem.java.strayfinder.service.ImgRefService;
import aem.java.strayfinder.web.model.ImgRefDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
public class ImageResource {

    private final ImgRefService imgRefService;

    public ImageResource(ImgRefService imgRefService) {
        this.imgRefService = imgRefService;
    }

    @PostMapping(value = "/img", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImgRefDTO> addNewImage(@Valid @RequestPart ImgRefDTO image, @RequestPart("file") MultipartFile file) throws IOException {
        ImgRefDTO imgRef = imgRefService.uploadFile(image, file);
        return ResponseEntity.ok(imgRef);
    }

    @GetMapping("/img/{id}")
    public ResponseEntity<ImgRefDTO> getImage(@PathVariable String id) {
        return ResponseEntity.ok(
                imgRefService.getImgRefById(id)
        );
    }
}
