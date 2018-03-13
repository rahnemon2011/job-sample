package com.kmv.geological.domain.entity;

import com.kmv.geological.domain.enums.JobStatus;
import com.kmv.geological.domain.enums.JobType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Job Table
 * <p>
 * We create a job record for each excel file. So each record of this table refers to a excel file.
 * we process a job(excel file) and read all its records and save them in SectionEntity. so each record of this table
 * has a OneToMany to SectionEntity. but we don't need this annotation in this class so I avoid it.
 *
 * @author h.mohammadi
 */
@Table(name = "jobs", uniqueConstraints = @UniqueConstraint(name = "UNIQUE_JOBS_NAME", columnNames = {"name"}))
@Entity
public class JobEntity extends BaseEntity {

    /**
     * Name is optional. For file-based jobs we put the name of the file.
     */
    @Column(length = 128)
    private String name;

    /**
     * JPA Converter could be used here to store small long, but for simplicity the string is saved.
     */
    @Column(length = 128)
    @Enumerated(EnumType.STRING)
    private JobType type;

    @Column(length = 128)
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.STARTED;

    @Column(length = 512)
    private String description;

    public JobEntity() {
    }

    public JobEntity(String name, JobType type, JobStatus status) {
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JobType getType() {
        return type;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
