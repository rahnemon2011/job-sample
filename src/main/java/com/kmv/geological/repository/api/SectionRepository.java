package com.kmv.geological.repository.api;

import com.kmv.geological.domain.entity.SectionEntity;
import com.kmv.geological.repository.MiddleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author h.mohammadi
 */
public interface SectionRepository extends MiddleRepository<SectionEntity, Long> {

    Page<SectionEntity> findByJobId(Long jobId, Pageable pageable);
}
