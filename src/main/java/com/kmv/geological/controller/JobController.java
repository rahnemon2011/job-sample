package com.kmv.geological.controller;

import com.kmv.geological.domain.dto.job.JobResponseDTO;
import com.kmv.geological.domain.entity.JobEntity;
import com.kmv.geological.service.job.JobService;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author h.mohammadi
 */
@RestController
@RequestMapping("/jobs")
public class JobController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobController.class.getName());

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/register-job")
    public Long registerJobAndProcessFile(@RequestParam(value = "sections") MultipartFile file)
            throws IOException {
        JobEntity jobEntity = jobService.createJob(file.getOriginalFilename());

        LOGGER.info("file with the name {} and jobId {} is created.", file.getOriginalFilename(), jobEntity.getId());

        jobService.processJob(jobEntity, file.getInputStream());

        return jobEntity.getId();
    }

    @PostMapping("/job-result")
    public JobResponseDTO jobResult(@RequestParam("job-id") Long id) {
        return jobService.findJob(id);
    }

}
