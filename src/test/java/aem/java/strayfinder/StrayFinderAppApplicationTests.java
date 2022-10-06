package aem.java.strayfinder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class StrayFinderAppApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainTest() {
        assertDoesNotThrow(() -> StrayFinderAppApplication.main(new String[]{}));
    }

}
