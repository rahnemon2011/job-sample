package com.kmv.geological.domain.dto.job;

import com.kmv.geological.domain.dto.BaseResponseDTO;
import com.kmv.geological.domain.enums.JobStatus;
import com.kmv.geological.domain.enums.JobType;

/**
 * @author h.mohammadi
 */
public class JobResponseDTO extends BaseResponseDTO {

    private String name;

    private JobType type;

    private JobStatus status;

    public JobResponseDTO() {
    }

    public JobResponseDTO(Long id, String name, JobType type, JobStatus status) {
        super(id);
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public JobResponseDTO(String name, JobType type, JobStatus status) {
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

}
