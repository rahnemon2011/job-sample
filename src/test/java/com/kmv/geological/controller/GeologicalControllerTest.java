package com.kmv.geological.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kmv.geological.BaseTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;

import com.kmv.geological.domain.dto.geologicalclass.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;
import com.kmv.geological.domain.entity.GeologicalEntity;
import com.kmv.geological.domain.entity.SectionEntity;
import com.kmv.geological.repository.api.SectionRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Hadi Mohammadi
 */
public class GeologicalControllerTest extends BaseTest {

    private static final String GEOLOGICAL_RESOURCE_URL = "/geological";

    @Autowired
    private SectionRepository sectionRepository;

    @Before
    public void init() {
        SectionEntity sectionEntity = new SectionEntity("Section 1");

        List<GeologicalEntity> geologicalEntities = new ArrayList<>();
        GeologicalEntity g1 = new GeologicalEntity("geo class 1", "code1", sectionEntity);
        GeologicalEntity g2 = new GeologicalEntity("geo class2", "code2", sectionEntity);
        GeologicalEntity g3 = new GeologicalEntity("anotherGeo", "anotherCode", sectionEntity);
        geologicalEntities.addAll(Arrays.asList(g1, g2, g3));

        sectionEntity.setGeologicalClasses(geologicalEntities);
        sectionRepository.save(sectionEntity);
    }

    @Test
    public void filter_simpleFilter_ok() throws Exception {
        String name = "geo class";

        MvcResult mvcResult = mockMvc.perform(get(GEOLOGICAL_RESOURCE_URL + "/filter")
                .contentType(MediaType.TEXT_PLAIN)
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
