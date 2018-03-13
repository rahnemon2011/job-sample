package com.kmv.geological;

import com.kmv.geological.domain.dto.job.JobResponseDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.kmv.geological.domain.enums.JobStatus.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {Application.class, JobIntegrationTest.class}
)
@ActiveProfiles("test")
public class JobIntegrationTest {

    private static final String JOB_RESOURCE_URL = "/jobs";
    private static final String FILE_NAME = "sections.xlsx";

    @Autowired
    private TestRestTemplate restTemplate;

    private Long jobId;

    public void setup() throws Exception {
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("sections", getUserFileResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        final ResponseEntity<Long> response =
                restTemplate.postForEntity(JOB_RESOURCE_URL + "/register-job", requestEntity, Long.class);
        jobId = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(jobId, greaterThan(0L));
    }

    public Resource getUserFileResource() throws IOException {
        String path = this.getClass().getClassLoader().getResource("files/" + FILE_NAME).getFile();
        File file = new File(path);
        //to upload in-memory bytes use ByteArrayResource instead
        return new FileSystemResource(file);
    }

    @Test
    public void saveAndFindJob_newJob_ok() throws Exception {
        setup();

        MultiValueMap<String, Long> map = new LinkedMultiValueMap();
        map.add("job-id", jobId);
        HttpEntity<MultiValueMap<String, Long>> request = new HttpEntity<MultiValueMap<String, Long>>(map);

        ResponseEntity<JobResponseDTO> jobResponseDTOResponseEntity =
                restTemplate.exchange(JOB_RESOURCE_URL + "/job-result", HttpMethod.POST, request, JobResponseDTO.class);
        JobResponseDTO jobResponseDTO = jobResponseDTOResponseEntity.getBody();

        assertThat(jobResponseDTO.getStatus(), isIn(Arrays.asList(STARTED, IN_PROGRESS, COMPLETED, FAILED, NO_CONTENT)));
    }

} 