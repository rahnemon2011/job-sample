package com.kmv.geological.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Section Table
 * <p>
 * Each record of Job(Excel file) has multi sections that we save them in this table.
 *
 * @author h.mohammadi
 */
@Entity
@Table(name = "sections",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "UNIQUE_SECTION_NAME")},
        indexes = {@Index(name = "INDEX_SECTION_NAME", columnList = "name")}
)
public class SectionEntity extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 128)
    private String name;

    //    @JoinColumn(name = "section", foreignKey = @ForeignKey(name = "FK_SECTIONS_ID"))
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section")
    private List<GeologicalEntity> geologicalClasses;

    @JoinColumn(name = "job_id", foreignKey = @ForeignKey(name = "FK_JOB_ID"))
    @ManyToOne()
    private JobEntity job;

    public SectionEntity() {
    }

    public SectionEntity(final Long id) {
        super(id);
    }

    public SectionEntity(String name) {
        this.name = name;
    }

    public SectionEntity(String name, JobEntity jobEntity) {
        this.name = name;
        this.job = jobEntity;
    }

    public SectionEntity(Long id, String name, JobEntity jobEntity) {
        super(id);
        this.name = name;
        this.job = jobEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GeologicalEntity> getGeologicalClasses() {
        return geologicalClasses;
    }

    public void setGeologicalClasses(List<GeologicalEntity> geologicalClasses) {
        this.geologicalClasses = geologicalClasses;
    }

    public JobEntity getJob() {
        return job;
    }

    public void setJob(JobEntity job) {
        this.job = job;
    }

}
