package com.kmv.geological.service.geological;

import com.kmv.geological.domain.dto.geological.GeologicalFilterRequestDTO;
import com.kmv.geological.domain.dto.geological.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;
import com.kmv.geological.exception.BusinessException;
import com.kmv.geological.repository.api.GeologicalRepository;
import com.kmv.geological.service.geological.GeologicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author h.mohammadi
 */
@Service
public class GeologicalServiceImpl implements GeologicalService {

    private final GeologicalRepository geologicalClassRepository;

    @Autowired
    public GeologicalServiceImpl(GeologicalRepository geologicalClassRepository) {
        this.geologicalClassRepository = geologicalClassRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public SimplePageResponseDTO<GeologicalResponseDTO> filter(
            GeologicalFilterRequestDTO requestDTO) {
        try {
            return geologicalClassRepository.filter(requestDTO);
        } catch (DataAccessException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

}
