package aem.java.strayfinder.service;

import aem.java.strayfinder.persistence.model.Stray;
import aem.java.strayfinder.persistence.model.StrayType;
import aem.java.strayfinder.persistence.model.Stray_;
import aem.java.strayfinder.persistence.model.Tag;
import aem.java.strayfinder.persistence.model.Tag_;
import aem.java.strayfinder.persistence.repository.StrayRepository;
import aem.java.strayfinder.service.filter.StringFilter;
import aem.java.strayfinder.web.model.StrayCriteria;
import aem.java.strayfinder.web.model.StrayDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.SetJoin;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class StrayService {

    private final StrayMapper strayMapper;
    private final StrayRepository strayRepository;
    private final TagService tagService;

    public StrayService(StrayMapper strayMapper, StrayRepository strayRepository, TagService tagService) {
        this.strayMapper = strayMapper;
        this.strayRepository = strayRepository;
        this.tagService = tagService;
    }

    public StrayDTO save(StrayDTO strayDTO) {
        log.debug("saving stray {}", strayDTO);
        Stray stray = strayMapper.toEntity(strayDTO);
        Set<Tag> existingTags = tagService.findAndInsertTags(strayDTO.getTags());
        stray.setTags(existingTags);

        stray = strayRepository.save(stray);
        return strayMapper.toDto(stray);
    }

    @Transactional(readOnly = true)
    public Page<StrayDTO> findByCriteria(StrayCriteria criteria, Pageable pageable) {
        log.debug("find by criteria: {}, pageable: {}", criteria, pageable);
        Specification<Stray> specification = buildSpecification(criteria);
        return strayRepository
                .findAll(specification, pageable)
                .map(strayMapper::toDto);
    }

    private Specification<Stray> buildSpecification(StrayCriteria criteria) {
        Specification<Stray> specification = (root, query, builder) -> {
            query.from(Stray.class);
            query.distinct(true);
            return null;
        };

        if (criteria != null) {
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringFilter(criteria.getDescription(), Stray_.description.getName()));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringFilter(criteria.getType(), Stray_.type.getName()));
            }
            if (criteria.getTags() != null && !criteria.getTags().isEmpty()) {
                Specification<Stray> sp = (root, query, criteriaBuilder) -> {
                    SetJoin<Stray, Tag> join = root.join(Stray_.tags, JoinType.INNER);
                    In<String> in = criteriaBuilder.in(join.get(Tag_.name));
                    for (String tagName :
                            criteria.getTags()) {
                        in = in.value(tagName);
                    }
                    return in;
                };
                specification = specification.and(sp);
            }
        }
        return specification;
    }


    private Specification<Stray> buildStringFilter(StringFilter filter, String field) {
        if (filter.getContains() != null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), wrapStringLikeStatement(filter.getContains()));
        } else if (filter.getEquals() != null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), StrayType.fromString(filter.getEquals()));
        } else if (filter.getNotEquals() != null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(field), StrayType.fromString(filter.getNotEquals()));
        } else if (filter.getNotContains() != null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.notLike(criteriaBuilder.upper(root.get(field)), wrapStringLikeStatement(filter.getNotContains()));
        }
        return null;
    }

    private String wrapStringLikeStatement(String value) {
        return ("%" + value + "%").toUpperCase();
    }
}
