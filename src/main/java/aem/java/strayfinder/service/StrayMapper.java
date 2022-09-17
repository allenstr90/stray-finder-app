package aem.java.strayfinder.service;

import aem.java.strayfinder.persistence.model.Stray;
import aem.java.strayfinder.web.model.StrayDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StrayMapper extends EntityMapper<StrayDTO, Stray> {
}
