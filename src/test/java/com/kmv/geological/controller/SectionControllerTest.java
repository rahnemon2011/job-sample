package com.kmv.geological.controller;

import com.kmv.geological.config.JacksonMapperConfig;
import com.kmv.geological.domain.dto.section.SectionWithGeologicalList;
import com.kmv.geological.domain.entity.GeologicalEntity;
import com.kmv.geological.domain.entity.JobEntity;
import com.kmv.geological.domain.entity.SectionEntity;
import com.kmv.geological.domain.enums.JobStatus;
import com.kmv.geological.domain.enums.JobType;
import com.kmv.geological.BaseTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.kmv.geological.repository.api.JobRepository;
import com.kmv.geological.repository.api.SectionRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author h.mohammadi
 */
public class SectionControllerTest extends BaseTest {

    private static final String SECTION_RESOURCE_URL = "/sections";

    @Autowired
    private SectionRepository sectionRepository;

    private Long sectionId;

    @Before
    public void init() {

        List<SectionEntity> sectionEntities = new ArrayList<>();
        SectionEntity sectionEntity1 = new SectionEntity("Section 1");
        SectionEntity sectionEntity2 = new SectionEntity("Section 2");
        SectionEntity sectionEntity3 = new SectionEntity("Section 3");
        sectionEntities.addAll(Arrays.asList(sectionEntity1, sectionEntity2, sectionEntity3));

        sectionRepository.save(sectionEntities);
        sectionId = sectionEntities.get(0).getId();
    }

    @Test
    public void post_simpleSection_ok() throws Exception {
        SectionWithGeologicalList requestDTO = new SectionWithGeologicalList("rest section");

        mockMvc.perform(post(SECTION_RESOURCE_URL)
                .content(JacksonMapperConfig.getObjectMapper().writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void post_emptySection_badRequest() throws Exception {
        SectionWithGeologicalList requestDTO = new SectionWithGeologicalList();

        mockMvc.perform(post(SECTION_RESOURCE_URL)
                .content(JacksonMapperConfig.getObjectMapper().writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    // 409 for trying to add duplicate record
    @Test
    public void post_duplicateSection_conflictCode() throws Exception {
        SectionWithGeologicalList requestDTO = new SectionWithGeologicalList("Section 1");

        mockMvc.perform(post(SECTION_RESOURCE_URL)
                .content(JacksonMapperConfig.getObjectMapper().writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

    }

    @Test
    public void post_nonExistingSection_notFoundCode() throws Exception {
        SectionWithGeologicalList requestDTO = new SectionWithGeologicalList(Long.MAX_VALUE, "Section doesn't exist");

        mockMvc.perform(post(SECTION_RESOURCE_URL)
                .content(JacksonMapperConfig.getObjectMapper().writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void searchById_existingSection_ok() throws Exception {
        mockMvc.perform(post(SECTION_RESOURCE_URL + "/details")
                .content(JacksonMapperConfig.getObjectMapper().writeValueAsString(sectionId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void searchById_sectionByZeroId_notFound() throws Exception {
        Long id = 0L;

        MvcResult result = mockMvc.perform(post(SECTION_RESOURCE_URL + "/details")
                .content(JacksonMapperConfig.getObjectMapper().writeValueAsString(id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void searchById_nonExistingSetion_notFound() throws Exception {
        Long id = Long.MAX_VALUE;

        mockMvc.perform(post(SECTION_RESOURCE_URL + "/details")
                .content(JacksonMapperConfig.getObjectMapper().writeValueAsString(id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
