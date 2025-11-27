package aem.java.strayfinder.service;

import aem.java.strayfinder.persistence.stray.model.Tag;
import aem.java.strayfinder.persistence.stray.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Set<Tag> findAndInsertTags(Set<String> names) {
        if (names == null || names.isEmpty())
            return null;

        Set<Tag> existingTags = tagRepository.findAllByNameIn(names);
        for (Tag tag : existingTags) {
            names.remove(tag.getName());
        }
        if (!names.isEmpty()) {
            List<Tag> newTags = names.stream().map(Tag::new).collect(Collectors.toList());
            newTags = tagRepository.saveAllAndFlush(newTags);
            existingTags.addAll(newTags);
        }
        return existingTags;
    }
}
