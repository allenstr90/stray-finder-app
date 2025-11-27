package aem.java.strayfinder.service;

import aem.java.strayfinder.errors.ImageNotFoundException;
import aem.java.strayfinder.errors.InvalidImageReferenceException;
import aem.java.strayfinder.persistence.images.ImageRef;
import aem.java.strayfinder.persistence.images.ImgRefRepository;
import aem.java.strayfinder.web.model.ImgRefDTO;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImgRefService {
    private final ImgRefRepository imgRefRepository;
    private final ImageRefMapper imageRefMapper;

    public ImgRefService(ImgRefRepository imgRefRepository, ImageRefMapper imageRefMapper) {
        this.imgRefRepository = imgRefRepository;
        this.imageRefMapper = imageRefMapper;
    }

    public ImgRefDTO uploadFile(ImgRefDTO imgRefDTO, MultipartFile file) throws IOException {
        ImageRef imageRef = imageRefMapper.toEntity(imgRefDTO);
        imageRef.setImage(
                new Binary(BsonBinarySubType.BINARY, file.getBytes())
        );
        imageRef = imgRefRepository.insert(imageRef);
        return imageRefMapper.toDto(imageRef);
    }

    public ImgRefDTO getImgRefById(String id) {
        Optional<ImageRef> imageById = imgRefRepository.findById(id);
        if (imageById.isPresent()) {
            return imageRefMapper.toDto(imageById.get());
        }
        throw new ImageNotFoundException();
    }
}
