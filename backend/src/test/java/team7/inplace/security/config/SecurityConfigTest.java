package team7.inplace.security.config;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("모두 허용으로 인해 실패하는 테스트 입니다.")
    @WithMockUser(roles = "USER")
    void testAdminEndpoints() throws Exception {
        mockMvc.perform(get("/admin/video"))
                .andExpect(header().string("Location", containsString("inplace.my")));
        mockMvc.perform(get("/admin/error-logs"))
                .andExpect(header().string("Location", containsString("inplace.my")));
        mockMvc.perform(post("/api/error-logs/1/video"))
                .andExpect(header().string("Location", containsString("inplace.my")));
        mockMvc.perform(get("/cicd"))
                .andExpect(header().string("Location", containsString("inplace.my")));
        mockMvc.perform(post("/crawling/video/1/place/1"))
                .andExpect(header().string("Location", containsString("inplace.my")));
    }

    @Test
    @DisplayName("모두 허용으로 인해 실패하는 테스트 입니다.")
    @WithAnonymousUser
    void testAnonymousEndpoints() throws Exception {
        mockMvc.perform(get("/admin/video"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/admin/error-logs"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/error-logs/1/video"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/cicd"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/influencers/likes"))
                .andExpect(status().isUnauthorized());
    }
}
