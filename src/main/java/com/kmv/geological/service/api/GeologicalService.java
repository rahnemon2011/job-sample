package com.kmv.geological.service.api;

import com.kmv.geological.domain.dto.geologicalclass.GeologicalFilterRequestDTO;
import com.kmv.geological.domain.dto.geologicalclass.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;

/**
 * @author h.mohammadi
 */
public interface GeologicalService {

    SimplePageResponseDTO<GeologicalResponseDTO> filter(GeologicalFilterRequestDTO requestDTO);
}
