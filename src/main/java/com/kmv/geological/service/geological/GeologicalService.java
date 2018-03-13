package com.kmv.geological.service.geological;

import com.kmv.geological.domain.dto.geological.GeologicalFilterRequestDTO;
import com.kmv.geological.domain.dto.geological.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;

/**
 * @author h.mohammadi
 */
public interface GeologicalService {

    SimplePageResponseDTO<GeologicalResponseDTO> filter(GeologicalFilterRequestDTO requestDTO);
}
