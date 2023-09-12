package dev.hub.mockmvc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.webeb.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class TestMockMvc {

    @Autowired
    private MockMvc mockMvc;

    /*MockMvc*/
    @Test
    public void testMockMvc() {
        log.debug("==== mockmvc test error contextLoaderListener regist ====");
//        mockMvc.perform(MockMvcRequestBuilders.get("/location"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
