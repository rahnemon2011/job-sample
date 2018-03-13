package com.kmv.geological.service;

import com.kmv.geological.BaseTest;
import com.kmv.geological.domain.dto.geologicalclass.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageRequestDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;
import com.kmv.geological.domain.dto.section.SectionByJobRequestDTO;
import com.kmv.geological.domain.dto.section.SectionWithGeologicalList;
import com.kmv.geological.domain.dto.section.SimpleSectionDTO;
import com.kmv.geological.domain.entity.GeologicalEntity;
import com.kmv.geological.domain.entity.JobEntity;
import com.kmv.geological.domain.entity.SectionEntity;
import com.kmv.geological.domain.enums.JobStatus;
import com.kmv.geological.domain.enums.JobType;
import com.kmv.geological.exception.general.DuplicateResourceException;
import com.kmv.geological.exception.general.NoSuchResourceException;
import com.kmv.geological.repository.api.JobRepository;
import com.kmv.geological.repository.api.SectionRepository;
import com.kmv.geological.service.api.SectionService;

import java.util.*;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author h.mohammadi
 */
public class SectionServiceTest extends BaseTest {

    @Autowired
    private SectionService sectionService;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private JobRepository jobRepository;

    private Long jobId;
    private Long sectionId;

    @Before
    public void init() {
        JobEntity jobEntity = new JobEntity("FileProcessJob", JobType.FILE_READING, JobStatus.STARTED);
        JobEntity save = jobRepository.save(jobEntity);
        jobId = save.getId();

        List<SectionEntity> sectionEntities = new ArrayList<>();
        SectionEntity sectionEntity1 = new SectionEntity("Section 1", save);
        SectionEntity sectionEntity2 = new SectionEntity("Section 2", save);
        SectionEntity sectionEntity3 = new SectionEntity("Section 3", save);
        sectionEntities.addAll(Arrays.asList(sectionEntity1, sectionEntity2, sectionEntity3));

        List<GeologicalEntity> geologicalEntities = new ArrayList<>();
        GeologicalEntity g = new GeologicalEntity("geo1", "code1", sectionEntity1);
        geologicalEntities.addAll(Collections.singletonList(g));

        sectionEntity1.setGeologicalClasses(geologicalEntities);

        sectionRepository.save(sectionEntities);
        sectionId = sectionEntities.get(0).getId();
    }

    @Test
    public void save_simpleSection_succeed() {
        SectionWithGeologicalList sectionEntity = new SectionWithGeologicalList("Section_save");
        SectionWithGeologicalList expectedEntity = null;

        expectedEntity = sectionService.saveOrUpdate(sectionEntity);

        Assert.assertNotEquals(expectedEntity, null);
        Assert.assertEquals(expectedEntity.getName(), sectionEntity.getName());
        Assert.assertNotNull(expectedEntity.getId());
    }

    @Test
    public void save_sectionWithGeological_succeed() {
        SectionWithGeologicalList sectionEntity = new SectionWithGeologicalList("Section_save");
        sectionEntity.setGeologicalClassList(Arrays.asList(new GeologicalResponseDTO("Class 1", "CG1"), new GeologicalResponseDTO("Class 2", "CG2")));
        SectionWithGeologicalList expectedEntity = null;

        expectedEntity = sectionService.saveOrUpdate(sectionEntity);

        Assert.assertNotEquals(expectedEntity, null);
        Assert.assertEquals(expectedEntity.getName(), sectionEntity.getName());
        Assert.assertNotNull(expectedEntity.getGeologicalClassList());
        Assert.assertEquals(expectedEntity.getGeologicalClassList().size(), 2);
    }

    @Test(expected = DuplicateResourceException.class)
    public void save_duplicateSectionName_throwException() {
        SectionWithGeologicalList sectionEntity = new SectionWithGeologicalList("Section_save");
        SectionWithGeologicalList duplicateSection = new SectionWithGeologicalList("Section_save");

        sectionService.saveOrUpdate(sectionEntity);
        sectionService.saveOrUpdate(duplicateSection);
    }

    @Test
    public void update_sectionName_succeed() {
        String updatedSectionName = "Section Updated";
        SectionWithGeologicalList sectionDTO = new SectionWithGeologicalList(updatedSectionName);
        sectionDTO.setId(sectionId);
        SectionWithGeologicalList expectedSecion = null;

        expectedSecion = sectionService.saveOrUpdate(sectionDTO);

        Assert.assertNotEquals(expectedSecion, null);
        Assert.assertEquals(expectedSecion.getName(), sectionDTO.getName());
        Assert.assertEquals(expectedSecion.getId(), sectionDTO.getId());
    }

    @Test
    public void update_GeologicalClasses_succeed() {
        String updatedSectionName = "Section 1";
        SectionWithGeologicalList sectionDTO = new SectionWithGeologicalList(updatedSectionName);
        sectionDTO.setId(sectionId);
        sectionDTO.setGeologicalClassList(Arrays.asList(new GeologicalResponseDTO(sectionId, "Class 1", "code1"), new GeologicalResponseDTO("Class 2", "CG2")));

        SectionWithGeologicalList expectedSecion = null;

        expectedSecion = sectionService.saveOrUpdate(sectionDTO);

        Assert.assertNotEquals(expectedSecion, null);
        Assert.assertEquals(expectedSecion.getName(), sectionDTO.getName());
        Assert.assertEquals(expectedSecion.getId(), sectionDTO.getId());
        Assert.assertNotEquals(expectedSecion.getGeologicalClassList(), null);
        Assert.assertEquals(2, expectedSecion.getGeologicalClassList().size());
        Assert.assertEquals("Class 1", expectedSecion.getGeologicalClassList().get(0).getName());
    }

    @Test(expected = NoSuchResourceException.class)
    public void update_nonExistingSection_throwsException() {
        String updatedSctionName = "Section Updated";
        SectionWithGeologicalList sectionDTO = new SectionWithGeologicalList(updatedSctionName);
        sectionDTO.setId(Long.MIN_VALUE);
        SectionWithGeologicalList expectedSecion = null;

        expectedSecion = sectionService.saveOrUpdate(sectionDTO);
    }

    @Test
    public void remove_existingSection_succeed() {
        sectionService.remove(sectionId);
    }

    @Test(expected = NoSuchResourceException.class)
    public void remove_nonExistingSection_throwException() {
        Long id = Long.MAX_VALUE;

        sectionService.remove(id);
    }

    @Test
    public void findById_existingSection_succeed() {
        SectionWithGeologicalList expectedSection = null;

        expectedSection = sectionService.findById(sectionId);

        Assert.assertNotNull(expectedSection);
        Assert.assertEquals(sectionId, expectedSection.getId());
        Assert.assertNotNull(expectedSection.getGeologicalClassList());
    }

    @Test(expected = NoSuchResourceException.class)
    public void findById_nonExistingSection_succeed() {
        Long id = Long.MAX_VALUE;

        sectionService.findById(id);
    }

    @Test
    public void findAll_simplePage_succeed() {
        SimplePageRequestDTO pageRequest = new SimplePageRequestDTO(0, 10);
        SimplePageResponseDTO<SimpleSectionDTO> pageResponse;

        pageResponse = sectionService.findAll(pageRequest);

        Assert.assertNotNull(pageResponse);
        Assert.assertThat(pageResponse.getCount(), Matchers.greaterThan(0L));
        Assert.assertNotNull(pageResponse.getContent());
        Assert.assertThat(pageResponse.getContent().size(), Matchers.greaterThan(0));
    }

    @Test
    public void findAll_pageNumberMoreThanExisting_emptyResult() {
        SimplePageRequestDTO pageRequest = new SimplePageRequestDTO(10000, 10);
        SimplePageResponseDTO<SimpleSectionDTO> pageResponse;

        pageResponse = sectionService.findAll(pageRequest);

        Assert.assertNotNull(pageResponse);
        Assert.assertEquals(0, pageResponse.getCount());
        Assert.assertNull(pageResponse.getContent());
    }

    @Test
    public void findAllByJob_simplePageWithExistingJob_succeed() {
        SectionByJobRequestDTO requestDTO = new SectionByJobRequestDTO(jobId, 0, 10);
        SimplePageResponseDTO<SimpleSectionDTO> pageResponse;

        pageResponse = sectionService.findAllByJob(requestDTO);

        Assert.assertNotNull(pageResponse);
        Assert.assertThat(pageResponse.getCount(), Matchers.greaterThan(0L));
        Assert.assertNotNull(pageResponse.getContent());
        Assert.assertThat(pageResponse.getContent().size(), Matchers.greaterThan(0));
    }

}
