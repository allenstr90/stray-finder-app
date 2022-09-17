package aem.java.strayfinder.web;

import aem.java.strayfinder.persistence.model.StrayType;
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

    private StrayDTO stray;

    @BeforeEach
    void setUp() {
        stray = StrayDTO.builder()
                .description("test stray")
                .type(StrayType.DOG.name()).build();
    }

    @Test
    @DisplayName("Create a stray and return ok")
    public void shouldCreateAStray_andReturnOk() throws Exception {
        String jsonData = mapper.writeValueAsString(stray);
        this.mockMvc.perform(post("/api/v1/stray").contentType(MediaType.APPLICATION_JSON).content(jsonData))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"));
    }

}
