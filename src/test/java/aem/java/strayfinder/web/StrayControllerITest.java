package aem.java.strayfinder.web;

import aem.java.strayfinder.persistence.model.StrayType;
import aem.java.strayfinder.service.StrayService;
import aem.java.strayfinder.web.model.StrayDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StrayControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private StrayService strayService;

    private StrayDTO stray;

    @BeforeEach
    void setUp() {
        stray = StrayDTO.builder()
                .description("test stray")
                .type(StrayType.DOG.name())
                .tags(new HashSet<>(Arrays.asList("black", "larger")))
                .latitude(22.984982)
                .longitude(-82.466689)
                .build();
    }

    @Test
    @DisplayName("Create a stray and return ok")
    @Transactional
    public void shouldCreateAStray_andReturnOk() throws Exception {
        String jsonData = mapper.writeValueAsString(stray);
        this.mockMvc.perform(post("/api/v1/stray").contentType(MediaType.APPLICATION_JSON).content(jsonData))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Get stray list")
    public void shouldGetListStray_andReturnAlmostOne() throws Exception {
        strayService.save(stray);

        this.mockMvc.perform(get("/api/v1/stray").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[*].description").value(hasItem(stray.getDescription())));
    }

    @Test
    @DisplayName("filter stray by criteria")
    public void shouldFilterStrayBasedOnCriteria() throws Exception {
        strayService.save(StrayDTO.builder().description("st1").type("DOG").tags(new HashSet<>(Arrays.asList("black", "larger", "intelligent"))).build());
        strayService.save(StrayDTO.builder().description("st2").type("CAT").tags(new HashSet<>(Arrays.asList("white", "skinny"))).build());
        strayService.save(StrayDTO.builder().description("st3").type("OTHER").build());

        // description contains and type equals
        this.mockMvc.perform(get("/api/v1/stray?description.contains=st&type.equals=dog")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].description").value("st1"));

        // description not contains and type not equals
        this.mockMvc.perform(get("/api/v1/stray?description.notContains=1&type.notEquals=cat")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].description").value("st3"));
    }
}
