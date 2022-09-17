package aem.java.strayfinder.web.rest;

import aem.java.strayfinder.service.StrayService;
import aem.java.strayfinder.web.model.StrayDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

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
}
