package com.kmv.geological.service.section;

import com.kmv.geological.domain.dto.page.SimplePageRequestDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;
import com.kmv.geological.domain.dto.section.SectionByJobRequestDTO;
import com.kmv.geological.domain.dto.section.SectionWithGeologicalList;
import com.kmv.geological.domain.dto.section.SimpleSectionDTO;

/**
 * @author h.mohammadi
 */
public interface SectionService {

    SectionWithGeologicalList saveOrUpdate(SectionWithGeologicalList entity);

    void remove(Long id);

    SectionWithGeologicalList findById(Long id);

    SimplePageResponseDTO<SimpleSectionDTO> findAll(SimplePageRequestDTO pageRequest);

    SimplePageResponseDTO<SimpleSectionDTO> findAllByJob(SectionByJobRequestDTO requestDTO);

}
