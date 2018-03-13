package com.kmv.geological.service.job.jobservicetype;

import com.kmv.geological.domain.enums.JobType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * depends of the jobType we return the specified service.
 */
@Component
public class JobServiceFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public IJobServiceType getJobService(JobType jobType) {
        switch (jobType) {
            case EXCEL_FILE_READING:
                return applicationContext.getBean(ExcelJobService.class);
            case XML_FILE_READING:
                return applicationContext.getBean(XMLJobService.class);
        }
        throw new UnsupportedOperationException("Unsupported JobType!");
    }
} 