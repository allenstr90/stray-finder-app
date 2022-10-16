package aem.java.strayfinder.web.rest;

import aem.java.strayfinder.errors.StrayNotFoundException;
import aem.java.strayfinder.service.ImgRefService;
import aem.java.strayfinder.web.model.ImgRefDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping(value = "/img/{strayId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImgRefDTO> addNewImgReference(@PathVariable("strayId") Long strayId, @Valid @RequestBody ImgRefDTO imgRefDTO) throws StrayNotFoundException, URISyntaxException {
        ImgRefDTO imgRef = imgRefService.addNewImgRef(strayId, imgRefDTO);
        return ResponseEntity.created(new URI("/api/v1/img/" + strayId)).body(imgRef);
    }

    @PostMapping("/img/{id}/upload")
    public ResponseEntity<ImgRefDTO> uploadImg(@PathVariable("id") Long imgRefId, @RequestParam("file") MultipartFile file) throws IOException {
        ImgRefDTO imgRef = imgRefService.uploadFile(imgRefId, file);
        return ResponseEntity.ok(imgRef);
    }
}
