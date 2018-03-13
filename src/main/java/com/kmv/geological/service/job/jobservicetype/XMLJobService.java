package com.kmv.geological.service.job.jobservicetype;

import com.kmv.geological.domain.entity.JobEntity;
import com.kmv.geological.repository.api.JobRepository;
import com.kmv.geological.repository.api.SectionRepository;
import com.kmv.geological.service.job.JobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.logging.Logger;

/**
 * processing the input xml file
 *
 * @author h.mohammadi
 */
@Component
public class XMLJobService implements IJobServiceType {

    private static final Logger LOGGER = Logger.getLogger(XMLJobService.class.getName());

    private JobRepository jobRepository;
    private SectionRepository sectionRepository;

    @Autowired
    public XMLJobService(JobRepository jobRepository, SectionRepository sectionRepository) {
        this.jobRepository = jobRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public void doJobProcess(JobEntity jobEntity, InputStream ioStream) {
        // TODO: 3/13/18 process the xml file.
    }
} 