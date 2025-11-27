package aem.java.strayfinder.service;

import aem.java.strayfinder.persistence.images.ImageRef;
import aem.java.strayfinder.web.model.ImgRefDTO;
import org.bson.types.Binary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Base64;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
uses = ImageToBase64.class)
public interface ImageRefMapper{

    @Mapping(source = "image", target = "imageBase64", qualifiedByName = "imageToBase64")
    ImgRefDTO toDto(ImageRef entity);

    @Mapping(target = "image", ignore = true)
    ImageRef toEntity(ImgRefDTO dto);

    @Named("imageToBase64")
    default String imageToBase64(Binary binary){
        return Base64.getEncoder().encodeToString(binary.getData());
    }
}
