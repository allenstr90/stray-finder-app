package aem.java.strayfinder.web;

import aem.java.strayfinder.persistence.images.ImageRef;
import aem.java.strayfinder.persistence.stray.model.StrayType;
import aem.java.strayfinder.persistence.images.ImgRefRepository;
import aem.java.strayfinder.service.ImgRefService;
import aem.java.strayfinder.service.StrayService;
import aem.java.strayfinder.web.model.ImgRefDTO;
import aem.java.strayfinder.web.model.StrayDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.MimeTypeUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ImageResourceITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private StrayService strayService;

    @Autowired
    private ImgRefService imgRefService;

    @Autowired
    private ImgRefRepository imgRefRepository;

    private ImgRefDTO imgRefDTO;

    @BeforeEach
    void setUp() {
        imgRefDTO = ImgRefDTO.builder()
                .id(UUID.randomUUID().toString())
                .name("img1")
                .strayId(1L)
                .mimeType(MimeTypeUtils.IMAGE_JPEG_VALUE)
                .build();
    }

    @Test
    void getNonExistingImage_shouldReturnNotFound() throws Exception {
        this.mockMvc.perform(
                        get("/api/v1/img/{id}", imgRefDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsBytes(imgRefDTO))
                )
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[*]").value(hasItem("Image not found.")));
    }

    @Test
    @Transactional
    void uploadImage_shouldStoreTheImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "lab.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                getClass().getResourceAsStream("/lab.jpg")
        );
        MockMultipartFile imgJson = new MockMultipartFile(
                "image",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(imgRefDTO)
        );
        this.mockMvc.perform(
                        multipart("/api/v1/img")
                                .file(file)
                                .file(imgJson)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(imgRefDTO.getName()))
                .andExpect(jsonPath("$.mimeType").value(imgRefDTO.getMimeType()))
                .andExpect(jsonPath("$.strayId").value(imgRefDTO.getStrayId()))
                .andExpect(jsonPath("$.imageBase64").isNotEmpty());

    }
}