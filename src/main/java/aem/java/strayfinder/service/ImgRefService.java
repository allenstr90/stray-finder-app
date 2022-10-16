package aem.java.strayfinder.service;

import aem.java.strayfinder.errors.InvalidImageReferenceException;
import aem.java.strayfinder.errors.StrayNotFoundException;
import aem.java.strayfinder.persistence.model.ImageRef;
import aem.java.strayfinder.persistence.repository.ImgRefRepository;
import aem.java.strayfinder.persistence.repository.StrayRepository;
import aem.java.strayfinder.web.model.ImgRefDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
public class ImgRefService {
    private final ImgRefRepository imgRefRepository;
    private final StrayRepository strayRepository;
    private final ImageRefMapper imageRefMapper;

    public ImgRefService(ImgRefRepository imgRefRepository, StrayRepository strayRepository, ImageRefMapper imageRefMapper) {
        this.imgRefRepository = imgRefRepository;
        this.strayRepository = strayRepository;
        this.imageRefMapper = imageRefMapper;
    }

    public ImgRefDTO addNewImgRef(Long strayId, ImgRefDTO imgRefDTO) throws StrayNotFoundException {
        ImageRef imageRef = strayRepository.findById(strayId)
                .map(stray -> {
                    ImageRef entity = imageRefMapper.toEntity(imgRefDTO);
                    entity.setStray(stray);
                    return entity;
                })
                .orElseThrow(StrayNotFoundException::new);
        imageRef = imgRefRepository.saveAndFlush(imageRef);
        return imageRefMapper.toDto(imageRef);
    }

    public ImgRefDTO uploadFile(Long imgRefId, MultipartFile file) throws IOException {
        ImageRef imageRef = imgRefRepository.findById(imgRefId)
                .orElseThrow(InvalidImageReferenceException::new);

        byte[] bytes = file.getBytes();
        imageRef.setBytes(bytes);
        imageRef.setHash(DigestUtils.md5DigestAsHex(bytes));

        imageRef = imgRefRepository.saveAndFlush(imageRef);
        return imageRefMapper.toDto(imageRef);
    }
}
