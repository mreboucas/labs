package com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith -> junit 4
@WebMvcTest(HelloWorldController.class)
@DisplayName("Hello Word COntroller")
public class HelloWorldControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void helloWorld_basic() throws Exception {
        //call GET "/hello-world"  application/json

        RequestBuilder request = MockMvcRequestBuilders
                .get("/hello-world")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                //Response Matches to check status and content
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andReturn();

        //verify "Hello World"
        assertEquals("Hello World", result.getResponse().getContentAsString());
    }

    public static void main(String[] args) {

        List contractChanges = new ArrayList();
        int s = Integer.valueOf(null);
        int as = ((Long) contractChanges.stream().filter(change -> !"".equals("")).count()).intValue();
    }


}