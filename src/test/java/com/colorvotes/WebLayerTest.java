package com.colorvotes;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WebLayerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ColorRepository colors;

    @Test
    void indexRendersAColorLinkPerColor() throws Exception {
        var page = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("TOTAL")));
        for (Color color : colors.findAll()) {
            page.andExpect(content().string(containsString(
                    "href=\"/api/colors/" + color.id() + "/votes\">" + color.name() + "</a>")));
        }
    }

    @Test
    void votesEndpointReturnsJson() throws Exception {
        Color red = colors.findAll().getFirst();
        mvc.perform(get("/api/colors/{id}/votes", red.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("Red"))
                .andExpect(jsonPath("$.votes").value(260_000));
    }

    @Test
    void votesEndpointReturns404ForUnknownColor() throws Exception {
        mvc.perform(get("/api/colors/999999/votes"))
                .andExpect(status().isNotFound());
    }

    @Test
    void votesEndpointReturns400ForNonNumericId() throws Exception {
        mvc.perform(get("/api/colors/red/votes"))
                .andExpect(status().isBadRequest());
    }
}
