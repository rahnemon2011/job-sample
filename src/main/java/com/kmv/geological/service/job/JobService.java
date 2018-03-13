package com.kmv.geological.service.job;

import com.kmv.geological.domain.dto.job.JobResponseDTO;
import com.kmv.geological.domain.entity.JobEntity;

import java.io.InputStream;

import org.springframework.scheduling.annotation.Async;

/**
 * Services for job
 * <p>
 * It has three below methods:
 * 1- creating a job for input excel file
 * 2- start processing the input excel file
 * 3- read a file to find out its status.
 *
 * @author h.mohammadi
 */
public interface JobService {

    /**
     * it creates a job for the input excel filename.
     *
     * @param excelFileName
     * @return
     */
    JobEntity createJob(String excelFileName);

    JobResponseDTO findJob(Long id);

    @Async
    void processJob(JobEntity jobEntity, InputStream inputStream);
}
