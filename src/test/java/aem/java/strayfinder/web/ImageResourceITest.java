package aem.java.strayfinder.web;

import aem.java.strayfinder.persistence.model.ImageRef;
import aem.java.strayfinder.persistence.model.StrayType;
import aem.java.strayfinder.persistence.repository.ImgRefRepository;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .name("img1")
                .mimeType(MimeTypeUtils.IMAGE_JPEG_VALUE)
                .build();
    }

    @Test
    void addImgRefShouldNotFound_whenNoStray() throws Exception {
        this.mockMvc.perform(
                        post("/api/v1/img/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsBytes(imgRefDTO))
                )
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[*]").value(hasItem("The stray doesn't exists")));
    }

    @Test
    @Transactional
    void addImgRefWithValidStray_shouldReturnCreated() throws Exception {
        StrayDTO strayDTO = StrayDTO.builder()
                .description("test stray")
                .type(StrayType.DOG.name())
                .tags(new HashSet<>(Arrays.asList("black", "larger")))
                .latitude(22.984982)
                .longitude(-82.466689)
                .build();
        strayDTO = strayService.save(strayDTO);

        this.mockMvc.perform(
                        post("/api/v1/img/{id}", strayDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsBytes(imgRefDTO))
                )
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(strayDTO.getId()))
                .andExpect(jsonPath("$.name").value(imgRefDTO.getName()))
                .andExpect(jsonPath("$.mimeType").value(imgRefDTO.getMimeType()));
    }

    @Test
    void uploadImage_shouldReturnBadRequest_whenInvalidImgRef() throws Exception{
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "lab.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                getClass().getResourceAsStream("/lab.jpg")
        );

        this.mockMvc.perform(
                        multipart("/api/v1/img/{id}/upload", 1L)
                                .file(file)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]").value(hasItem("Invalid image reference. You can't upload that image.")));
    }

    @Test
    @Transactional
    void uploadImage_shouldUpdateBytesAndHash() throws Exception {
        StrayDTO strayDTO = StrayDTO.builder()
                .description("test stray")
                .type(StrayType.DOG.name())
                .tags(new HashSet<>(Arrays.asList("black", "larger")))
                .latitude(22.984982)
                .longitude(-82.466689)
                .build();
        strayDTO = strayService.save(strayDTO);
        imgRefDTO = imgRefService.addNewImgRef(strayDTO.getId(), imgRefDTO);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "lab.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                getClass().getResourceAsStream("/lab.jpg")
        );

        this.mockMvc.perform(
                        multipart("/api/v1/img/{id}/upload", imgRefDTO.getId())
                                .file(file)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(strayDTO.getId()))
                .andExpect(jsonPath("$.name").value(imgRefDTO.getName()))
                .andExpect(jsonPath("$.mimeType").value(imgRefDTO.getMimeType()));

        ImageRef imageRef = imgRefRepository
                .findById(imgRefDTO.getId()).get();

        String md5DigestAsHex = DigestUtils.md5DigestAsHex(file.getBytes());
        Assertions.assertArrayEquals(imageRef.getBytes(), file.getBytes());
        Assertions.assertEquals(imageRef.getHash(), md5DigestAsHex);
    }
}