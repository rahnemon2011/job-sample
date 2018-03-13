package com.kmv.geological.service.job.jobservicetype;

import com.kmv.geological.domain.entity.JobEntity;

import java.io.InputStream;

public interface IJobServiceType {

    void doJobProcess(JobEntity jobEntity, InputStream ioStream);
} 