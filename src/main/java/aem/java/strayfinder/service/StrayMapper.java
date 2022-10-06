package aem.java.strayfinder.service;

import aem.java.strayfinder.persistence.model.Stray;
import aem.java.strayfinder.persistence.model.Tag;
import aem.java.strayfinder.web.model.StrayDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface StrayMapper extends EntityMapper<StrayDTO, Stray> {

    @Mapping(source = "longitude", target = "location.longitude")
    @Mapping(source = "latitude", target = "location.latitude")
    Stray toEntity(StrayDTO dto);

    @Mapping(source = "location.longitude", target = "longitude")
    @Mapping(source = "location.latitude", target = "latitude")
    StrayDTO toDto(Stray stray);

    default Set<Tag> toEntity(Set<String> strings) {
        if (strings == null)
            return null;
        Set<Tag> tags = new HashSet<>(strings.size());
        for (String tg :
                strings) {
            Tag tag = new Tag();
            tag.setName(tg);
            tags.add(tag);
        }
        return tags;
    }

    default Set<String> toDto(Set<Tag> tags) {
        if (tags == null)
            return null;
        Set<String> strings = new HashSet<>(tags.size());
        for (Tag tg : tags) {
            strings.add(tg.getName());
        }
        return strings;
    }
}
