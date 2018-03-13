package com.kmv.geological.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;

import com.kmv.geological.domain.dto.geological.GeologicalFilterRequestDTO;
import com.kmv.geological.domain.dto.geological.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;
import com.kmv.geological.service.geological.GeologicalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;

/**
 * @author h.mohammadi
 */
@RunWith(SpringRunner.class)
@WebMvcTest({GeologicalService.class, GeologicalController.class})
@AutoConfigureMockMvc
public class GeologicalControllerTest {

    private static final String GEOLOGICAL_RESOURCE_URL = "/geological";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeologicalService geologicalService;

    @Before
    public void init() {

        List<GeologicalResponseDTO> geologicalEntities = new ArrayList<>();
        GeologicalResponseDTO g1 = new GeologicalResponseDTO("geo class 1", "code1");
        GeologicalResponseDTO g2 = new GeologicalResponseDTO("geo class2", "code2");
        geologicalEntities.addAll(Arrays.asList(g1, g2));

        SimplePageResponseDTO<GeologicalResponseDTO> responseDTO =
                new SimplePageResponseDTO<>(geologicalEntities, 2);

        when(geologicalService.filter(any(GeologicalFilterRequestDTO.class)))
                .thenReturn(responseDTO);
    }

    @Test
    public void filter_simpleFilter_ok() throws Exception {
        String name = "geo class";

        MvcResult mvcResult = mockMvc.perform(get(GEOLOGICAL_RESOURCE_URL + "/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("name", name))

                .andExpect(status().isOk())
                .andExpect(content().string(containsString("geo class")))
                .andReturn();
        String responseJson = mvcResult.getResponse().getContentAsString();

        SimplePageResponseDTO<GeologicalResponseDTO> myObjects = objectMapper.readValue(responseJson,
                new TypeReference<SimplePageResponseDTO<GeologicalResponseDTO>>() {
                });
        List<GeologicalResponseDTO> geologicalResponseDTOS = myObjects.getContent();
        assertEquals("The count should be 2", 2, geologicalResponseDTOS.size());
    }

}