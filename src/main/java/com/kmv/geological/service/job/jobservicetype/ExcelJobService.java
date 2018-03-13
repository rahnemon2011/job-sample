package com.kmv.geological.service.job.jobservicetype;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kmv.geological.config.JacksonMapperConfig;
import com.kmv.geological.domain.entity.GeologicalEntity;
import com.kmv.geological.domain.entity.JobEntity;
import com.kmv.geological.domain.entity.SectionEntity;
import com.kmv.geological.domain.enums.JobStatus;
import com.kmv.geological.repository.api.JobRepository;
import com.kmv.geological.repository.api.SectionRepository;
import com.kmv.geological.service.job.JobServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * processing the input excel file
 *
 * @author h.mohammadi
 */
@Component
public class ExcelJobService implements IJobServiceType {

    private static final Logger LOGGER = Logger.getLogger(ExcelJobService.class.getName());

    private JobRepository jobRepository;
    private SectionRepository sectionRepository;

    @Autowired
    public ExcelJobService(JobRepository jobRepository, SectionRepository sectionRepository) {
        this.jobRepository = jobRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public void doJobProcess(JobEntity jobEntity, InputStream ioStream) {
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
                                    }
                            );

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
}