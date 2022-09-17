package aem.java.strayfinder.service;

import aem.java.strayfinder.persistence.model.Stray;
import aem.java.strayfinder.persistence.repository.StrayRepository;
import aem.java.strayfinder.web.model.StrayDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StrayService {
    private final Logger log = LoggerFactory.getLogger(StrayService.class);

    private final StrayMapper strayMapper;
    private final StrayRepository strayRepository;

    public StrayService(StrayMapper strayMapper, StrayRepository strayRepository) {
        this.strayMapper = strayMapper;
        this.strayRepository = strayRepository;
    }

    public StrayDTO save(StrayDTO strayDTO) {
        log.debug("saving stray {}", strayDTO);
        Stray stray = strayMapper.toEntity(strayDTO);
        stray = strayRepository.save(stray);
        return strayMapper.toDto(stray);
    }
}
