package com.kmv.geological.repository.custom;

import com.kmv.geological.domain.dto.geological.GeologicalFilterRequestDTO;
import com.kmv.geological.domain.dto.geological.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;

/**
 * @author h.mohammadi
 */
public interface GeologicalRepositoryCustom {

    SimplePageResponseDTO<GeologicalResponseDTO> filter(GeologicalFilterRequestDTO requestDTO);
}
