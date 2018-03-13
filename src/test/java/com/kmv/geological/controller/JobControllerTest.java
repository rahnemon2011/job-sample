package com.kmv.geological.controller;

import com.kmv.geological.config.JacksonMapperConfig;
import com.kmv.geological.domain.dto.job.JobResponseDTO;
import com.kmv.geological.BaseTest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import com.kmv.geological.domain.entity.JobEntity;
import com.kmv.geological.domain.enums.JobStatus;
import com.kmv.geological.domain.enums.JobType;
import com.kmv.geological.repository.api.JobRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author h.mohammadi
 */
public class JobControllerTest extends BaseTest {

    private static final String JOB_RESOURCE_URL = "/jobs";
    private static final String FILE_NAME = "sections.xls";

    @Autowired
    private JobRepository jobRepository;

    private Long jobId;

    @Before
    public void init() {
        JobEntity jobEntity = new JobEntity("FileProcessJob", JobType.FILE_READING, JobStatus.STARTED);
        JobEntity save = jobRepository.save(jobEntity);
        jobId = save.getId();
    }

    @Test
    public void registerJobAndProcessFile_simpleFile_ok() throws Exception {

        String path = this.getClass().getClassLoader().getResource("files/sections.xlsx").getFile();
        try (InputStream inputStream = new FileInputStream(path)) {
            MockMultipartFile file = new MockMultipartFile("sections", FILE_NAME,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);

            mockMvc.perform(fileUpload(JOB_RESOURCE_URL + "/register-job").file(file))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
        //It will avoid duplicate key exception! because, processExcelFile is running asynchronisely, multiple tests
        //will try to insert the same data of the same file at the same time! So, sleeping the thread does the trick!
//        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void findJobResult_existingJob_haveResult() throws Exception {

        String result = mockMvc.perform(post(JOB_RESOURCE_URL + "/job-result")
                .param("job-id", jobId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JobResponseDTO expectedJob = JacksonMapperConfig.getObjectMapper().readValue(result, JobResponseDTO.class);

        Assert.assertNotNull(expectedJob);
        Assert.assertEquals(expectedJob.getStatus(), JobStatus.STARTED);
    }

    @Test
    public void findJobResult_nonExistingJob_notFound() throws Exception {
        Long jobId = Long.MAX_VALUE;

        mockMvc.perform(post(JOB_RESOURCE_URL + "/job-result")
                .param("job-id", jobId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
