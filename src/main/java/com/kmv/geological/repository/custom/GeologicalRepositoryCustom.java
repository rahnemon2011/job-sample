package com.kmv.geological.repository.custom;

import com.kmv.geological.domain.dto.geologicalclass.GeologicalFilterRequestDTO;
import com.kmv.geological.domain.dto.geologicalclass.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;

/**
 * @author h.mohammadi
 */
public interface GeologicalRepositoryCustom {

    SimplePageResponseDTO<GeologicalResponseDTO> filter(GeologicalFilterRequestDTO requestDTO);
}
