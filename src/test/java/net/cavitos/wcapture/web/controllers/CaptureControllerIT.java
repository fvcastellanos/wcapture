package net.cavitos.wcapture.web.controllers;

import net.cavitos.wcapture.model.Capture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebAppConfiguration
@SpringBootTest
@TestPropertySource({"classpath:application.properties", "classpath:application-test.properties"})
public class CaptureControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

    @Test
    public void testGetCaptureMainView() throws Exception {
        mockMvc()
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"));
    }

    @Test
    public void testPostCaptureUrl() throws Exception {
        mockMvc()
                .perform(
                        post("/")
                                .contentType(APPLICATION_FORM_URLENCODED)
                                .param("url", "https://www.google.com"))

                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("capture", hasProperty("captureId", is(not(nullValue())))));
    }

    @Test
    public void testPostCapture_InvalidUrl() throws Exception {
        mockMvc()
                .perform(
                        post("/")
                                .contentType(APPLICATION_FORM_URLENCODED)
                                .param("url", "asdf1234"))

                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("error", is("Please provide a valid URL")));
    }

    @Test
    public void testGetCapturedUrl() throws Exception {
        final ResultActions postResultActions = mockMvc()
                .perform(
                        post("/")
                                .contentType(APPLICATION_FORM_URLENCODED)
                                .param("url", "https://www.google.com"));

        postResultActions.andExpect(status().isOk());
        postResultActions.andExpect(view().name("main"));
        postResultActions.andExpect(model().attribute("capture", hasProperty("captureId", is(not(nullValue())))));

        final MvcResult postMvcResult = postResultActions.andExpect(status().isOk()).andReturn();

        final Capture capture = (Capture) postMvcResult.getModelAndView().getModel().get("capture");

        final ResultActions resultActions = mockMvc()
                .perform(
                        get("/file/" + capture.getCaptureId()))
                .andExpect(status().isOk());


        final MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        assertThat(mvcResult.getResponse().getBufferSize(), is(not(nullValue())));
    }

    private MockMvc mockMvc() {
        return webAppContextSetup(webApplicationContext)
                .build();

    }

}
