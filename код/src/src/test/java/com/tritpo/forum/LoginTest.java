package com.tritpo.forum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginTest() throws Exception {
        this.mockMvc.perform(get("/myaccount"))
                .andDo(print())
                .andExpect((status().is3xxRedirection()))
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void correctUserTest() throws Exception {
        this.mockMvc.perform(formLogin().user("").password("4"))
                .andDo(print())
                .andExpect((status().is3xxRedirection()))
                .andExpect(redirectedUrl("/myaccount"));
    }

}
