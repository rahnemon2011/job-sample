package com.kmv.geological.service.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kmv.geological.config.JacksonMapperConfig;
import com.kmv.geological.domain.dto.job.JobResponseDTO;
import com.kmv.geological.domain.entity.GeologicalEntity;
import com.kmv.geological.domain.entity.JobEntity;
import com.kmv.geological.domain.entity.SectionEntity;
import com.kmv.geological.domain.enums.JobStatus;
import com.kmv.geological.domain.enums.JobType;
import com.kmv.geological.exception.BusinessException;
import com.kmv.geological.exception.general.NoSuchResourceException;
import com.kmv.geological.repository.api.JobRepository;
import com.kmv.geological.repository.api.SectionRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.kmv.geological.service.job.jobservicetype.JobServiceFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author h.mohammadi
 */
@Service
public class JobServiceImpl implements JobService {

    private static final Logger LOGGER = Logger.getLogger(JobServiceImpl.class.getName());
    private final JobRepository jobRepository;
    private final JobServiceFactory jobServiceFactory;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository,
                          JobServiceFactory jobServiceFactory) {
        this.jobRepository = jobRepository;
        this.jobServiceFactory = jobServiceFactory;
    }

    @Override
    @Transactional
    public JobEntity createJob(String excelFileName) {
        String uniqueName = new StringBuilder(excelFileName.length() > 60 ? excelFileName.substring(0, 60) : excelFileName)
                .append("-")
                .append(RandomStringUtils.randomAlphabetic(10))
                .append("-")
                .append(String.valueOf(System.currentTimeMillis()))
                .toString();

        String extension = excelFileName.substring(excelFileName.lastIndexOf('.') + 1, excelFileName.length());
        JobType jobType = ExtensionFileFactory.getJobType(extension);
        JobEntity jobEntity = new JobEntity(uniqueName, jobType, JobStatus.NOT_STARTED);

        try {
            return jobRepository.save(jobEntity);
        } catch (DataAccessException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    /**
     * depends of the jobType at first we retrieve the specified service and after that process it.
     * TODO: To be more optimized and more tested, it is better to use Spring Batch.
     *
     * @param jobEntity
     * @param ioStream
     */
    @Transactional
    @Override
    public void processJob(JobEntity jobEntity, InputStream ioStream) {
        jobServiceFactory.getJobService(jobEntity.getType())
                .doJobProcess(jobEntity, ioStream);
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponseDTO findJob(Long id) {
        if (id == null || id < 1) {
            throw new NoSuchResourceException("Job not found!");
        }
        try {
            JobEntity jobEntity = jobRepository.findOne(id);
            if (jobEntity == null) {
                throw new NoSuchResourceException("Job not found!");
            }
            return new JobResponseDTO(jobEntity.getName(), jobEntity.getType(), jobEntity.getStatus());
        } catch (DataAccessException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

}
