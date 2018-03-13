package com.kmv.geological.service;

import com.kmv.geological.domain.entity.JobEntity;
import com.kmv.geological.domain.enums.JobStatus;
import com.kmv.geological.service.api.JobService;
import com.kmv.geological.BaseTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author h.mohammadi
 */
public class JobServiceTest extends BaseTest {

    private static final String FILE_NAME = "sections.xls";

    @Autowired
    private JobService jobService;

    @Test
    public void registerJob_fileName_succeed() {
        JobEntity expectedJob = jobService.createJob(FILE_NAME);

        Assert.assertNotNull(expectedJob);
        Assert.assertThat(expectedJob.getId(), Matchers.greaterThan(0L));
    }

    @Test
    public void registerJobThenProcessExcelFile_simpleFile_succeed()
            throws FileNotFoundException, InterruptedException {

        String path = this.getClass().getClassLoader().getResource("files/sections.xlsx").getFile();

        InputStream inputStream = new FileInputStream(path);

        JobEntity jobEntity = jobService.createJob(FILE_NAME);
        jobService.processExcelFile(jobEntity, inputStream);

        Assert.assertNotNull(jobEntity);
        //It will avoid duplicate key exception! because, processExcelFile is running asynchronisely, multiple tests
        //will try to insert the same data of the same file at the same time! So, sleeping the thread does the trick!
        //Also, we can ensure that the job status is COMPLETED.
        TimeUnit.SECONDS.sleep(3);
        Assert.assertSame(JobStatus.COMPLETED, jobEntity.getStatus());
    }
}
