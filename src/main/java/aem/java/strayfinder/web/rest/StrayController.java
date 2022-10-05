package aem.java.strayfinder.web.rest;

import aem.java.strayfinder.service.PaginationUtil;
import aem.java.strayfinder.service.StrayService;
import aem.java.strayfinder.web.model.StrayCriteria;
import aem.java.strayfinder.web.model.StrayDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StrayController {

    private final Logger log = LoggerFactory.getLogger(StrayController.class);

    private final StrayService strayService;

    public StrayController(StrayService strayService) {
        this.strayService = strayService;
    }

    @PostMapping("/stray")
    public ResponseEntity<StrayDTO> createNewStray(@Valid @RequestBody StrayDTO strayDTO) throws URISyntaxException {
        log.debug("save new stray: {}", strayDTO);

        StrayDTO result = strayService.save(strayDTO);

        return ResponseEntity.created(new URI("/api/v1/stray/" + result.getId()))
                .body(result);

    }

    @GetMapping("/stray")
    public ResponseEntity<List<StrayDTO>> getAllStray(StrayCriteria criteria, Pageable pageable) {
        log.debug("get all stray by criteria: {}, pageable: {}", criteria, pageable);
        Page<StrayDTO> page = strayService.findByCriteria(criteria, pageable);
        HttpHeaders httpHeaders = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(httpHeaders).body(page.getContent());
    }
}
