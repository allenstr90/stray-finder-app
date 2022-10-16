package aem.java.strayfinder.service;

import aem.java.strayfinder.persistence.model.ImageRef;
import aem.java.strayfinder.web.model.ImgRefDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ImageRefMapper extends EntityMapper<ImgRefDTO, ImageRef> {
}
