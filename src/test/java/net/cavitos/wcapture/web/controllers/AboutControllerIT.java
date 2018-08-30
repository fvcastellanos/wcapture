package net.cavitos.wcapture.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebAppConfiguration
@SpringBootTest
@TestPropertySource({"classpath:application.properties", "classpath:application-test.properties", "classpath:git.properties"})
public class AboutControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

    @Test
    public void testGetAboutView() throws Exception {
        mockMvc()
                .perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(model().attribute("gitBranch", is(not(nullValue()))))
                .andExpect(model().attribute("gitBuildHost", is(not(nullValue()))))
                .andExpect(model().attribute("gitBuildTime", is(not(nullValue()))))
                .andExpect(model().attribute("gitBuildVersion", is(not(nullValue()))))
                .andExpect(model().attribute("gitCommitId", is(not(nullValue()))))
                .andExpect(model().attribute("gitCommitIdAbbrev", is(not(nullValue()))))
                .andExpect(model().attribute("gitCommitTime", is(not(nullValue()))))
                .andExpect(model().attribute("gitTags", is(not(nullValue()))));
    }

    private MockMvc mockMvc() {
        return webAppContextSetup(webApplicationContext)
                .build();

    }

}
