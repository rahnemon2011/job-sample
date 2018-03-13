package com.kmv.geological.repository.api;

import com.kmv.geological.domain.entity.GeologicalEntity;
import com.kmv.geological.repository.MiddleRepository;
import com.kmv.geological.repository.custom.GeologicalRepositoryCustom;

/**
 * @author h.mohammadi
 */
public interface GeologicalRepository extends MiddleRepository<GeologicalEntity, Long>, GeologicalRepositoryCustom {

}
