package com.kmv.geological.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kmv.geological.config.JacksonMapperConfig;
import com.kmv.geological.domain.dto.geologicalclass.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.job.JobResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;
import com.kmv.geological.domain.entity.GeologicalEntity;
import com.kmv.geological.domain.entity.JobEntity;
import com.kmv.geological.domain.entity.SectionEntity;
import com.kmv.geological.domain.enums.JobStatus;
import com.kmv.geological.domain.enums.JobType;
import com.kmv.geological.exception.BusinessException;
import com.kmv.geological.exception.general.NoSuchResourceException;
import com.kmv.geological.repository.api.JobRepository;
import com.kmv.geological.repository.api.SectionRepository;
import com.kmv.geological.service.api.JobService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final SectionRepository sectionRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository, SectionRepository sectionRepository) {
        this.jobRepository = jobRepository;
        this.sectionRepository = sectionRepository;
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

        JobEntity jobEntity = new JobEntity(uniqueName, JobType.FILE_READING, JobStatus.NOT_STARTED);

        try {
            return jobRepository.save(jobEntity);
        } catch (DataAccessException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    /**
     * TODO: To be more optimized and more tested, it is better to use Spring Batch.
     *
     * @param jobEntity
     * @param ioStream
     */
    @Transactional
    @Override
    public void processExcelFile(JobEntity jobEntity, InputStream ioStream) {
        try (InputStream inputStream = ioStream) {
            Workbook workbook = new XSSFWorkbook(inputStream);

            long rowCount = workbook.getSheetAt(0).getLastRowNum();
            if (rowCount < 2) {
                jobEntity.setStatus(JobStatus.NO_CONTENT);
                jobRepository.save(jobEntity);
                return;
            }
            Iterator<Row> iterator = workbook.getSheetAt(0).iterator();

            // to skip the first row which has titles
            iterator.next();

            // simple size for batch save
            List<SectionEntity> sectionEntities = new ArrayList<>(100);
            boolean isListSaved = false;

            // it determines that job is in progress.
            jobEntity.setStatus(JobStatus.IN_PROGRESS);
            jobRepository.save(jobEntity);

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                if (currentRow.getCell(0) == null && currentRow.getCell(0).getCellTypeEnum() != CellType.STRING) {
                    continue;
                }
                SectionEntity sectionEntity = new SectionEntity(currentRow.getCell(0).getStringCellValue());
                sectionEntity.setJob(jobEntity);
                Cell geoLogicalClassCell = currentRow.getCell(1);
                if (geoLogicalClassCell != null) {


                    List<GeologicalEntity> myObjects = JacksonMapperConfig.getObjectMapper()
                            .readValue(geoLogicalClassCell.getStringCellValue(),
                                    new TypeReference<List<GeologicalEntity>>() {
                                    });

//                    GeologicalResponseDTO[] gcdtos = JacksonMapperConfig.getObjectMapper()
//                            .readValue(geoLogicalClassCell.getStringCellValue(), GeologicalResponseDTO[].class);

//                    List<GeologicalEntity> list = Stream.of(gcdtos)
//                            .map(dto -> new GeologicalEntity(dto.getName(), dto.getCode()))
//                            .collect(Collectors.toList());
                    sectionEntity.setGeologicalClasses(myObjects);
                }
                sectionEntities.add(sectionEntity);
                if (sectionEntities.size() == 100) {
                    sectionRepository.save(sectionEntities);
                    isListSaved = true;
                    sectionEntities.clear();
                }
            }

            // If the loop is finished and the list size < 100, then this will insert the data
            if (!isListSaved && !sectionEntities.isEmpty()) {
                sectionRepository.save(sectionEntities);
            }
            jobEntity.setStatus(JobStatus.COMPLETED);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            jobEntity.setStatus(JobStatus.FAILED);
            jobEntity.setDescription(ex.getMessage());
        }
        jobRepository.save(jobEntity);
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
