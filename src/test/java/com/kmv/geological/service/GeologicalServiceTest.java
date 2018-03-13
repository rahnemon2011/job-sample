package com.kmv.geological.service;

import com.kmv.geological.BaseTest;
import com.kmv.geological.domain.dto.geologicalclass.GeologicalFilterRequestDTO;
import com.kmv.geological.domain.dto.geologicalclass.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;
import com.kmv.geological.domain.entity.GeologicalEntity;
import com.kmv.geological.domain.entity.JobEntity;
import com.kmv.geological.domain.entity.SectionEntity;
import com.kmv.geological.domain.enums.JobStatus;
import com.kmv.geological.domain.enums.JobType;
import com.kmv.geological.repository.api.JobRepository;
import com.kmv.geological.repository.api.SectionRepository;
import com.kmv.geological.service.api.GeologicalService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author h.mohammadi
 */
public class GeologicalServiceTest extends BaseTest {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private GeologicalService geologicalService;

    @Before
    public void init() {
        JobEntity jobEntity = new JobEntity("FileProcessJob", JobType.FILE_READING, JobStatus.STARTED);
        jobRepository.save(jobEntity);

        SectionEntity sectionEntity = new SectionEntity("Section 1");

        List<GeologicalEntity> geologicalEntities = new ArrayList<>();
        GeologicalEntity g1 = new GeologicalEntity("geo1", "code1", sectionEntity);
        GeologicalEntity g2 = new GeologicalEntity("geo2", "code2", sectionEntity);
        GeologicalEntity g3 = new GeologicalEntity("anotherGeo", "anotherCode", sectionEntity);
        geologicalEntities.addAll(Arrays.asList(g1, g2, g3));

        sectionEntity.setGeologicalClasses(geologicalEntities);
        sectionEntity.setJob(jobEntity);

        sectionRepository.save(sectionEntity);
    }

    @Test
    public void filter_simpleFilter_succeed() {
        int page = 0, size = 2;
        String name = "geo", code = "code";
        GeologicalFilterRequestDTO requestDTO = new GeologicalFilterRequestDTO(name, code, page, size);

        SimplePageResponseDTO<GeologicalResponseDTO> pageResponse = geologicalService.filter(requestDTO);

        Assert.assertNotNull(pageResponse);
        Assert.assertEquals(3, pageResponse.getCount());
        Assert.assertEquals(2, pageResponse.getContent().size());
    }

    @Test
    public void filter_simpleFilterWithNonExistingValues_empty() {
        int page = 0, size = 2;
        String name = "nonExisting";
        GeologicalFilterRequestDTO requestDTO = new GeologicalFilterRequestDTO(name, null, page, size);

        SimplePageResponseDTO<GeologicalResponseDTO> pageResponse = geologicalService.filter(requestDTO);

        Assert.assertNotNull(pageResponse);
        Assert.assertEquals(0, pageResponse.getCount());
        Assert.assertTrue(pageResponse.getContent().isEmpty());
    }
}
