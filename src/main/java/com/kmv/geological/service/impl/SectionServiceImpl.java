package com.kmv.geological.service.impl;

import com.kmv.geological.domain.dto.geologicalclass.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageRequestDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;
import com.kmv.geological.domain.dto.section.SectionByJobRequestDTO;
import com.kmv.geological.domain.dto.section.SectionWithGeologicalList;
import com.kmv.geological.domain.dto.section.SimpleSectionDTO;
import com.kmv.geological.domain.entity.GeologicalEntity;
import com.kmv.geological.domain.entity.SectionEntity;
import com.kmv.geological.exception.BusinessException;
import com.kmv.geological.exception.general.DuplicateResourceException;
import com.kmv.geological.exception.general.NoSuchResourceException;
import com.kmv.geological.repository.api.SectionRepository;
import com.kmv.geological.service.api.SectionService;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author h.mohammadi
 */
@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Transactional
    @Override
    public SectionWithGeologicalList saveOrUpdate(SectionWithGeologicalList sectionDTO) {
        try {
            SectionEntity entity = sectionDTO.getId() != null ? sectionRepository.findOne(sectionDTO.getId()) : new SectionEntity(sectionDTO.getName());
            if (entity == null) {
                throw new NoSuchResourceException("no such section to be found!");
            }
            if (sectionDTO.getGeologicalClassList() != null) {
                entity.setGeologicalClasses(sectionDTO.getGeologicalClassList().stream().
                        map(dto -> new GeologicalEntity(dto.getId(), dto.getName(), dto.getCode())).collect(Collectors.toList()));
            }
            entity = sectionRepository.save(entity);
            sectionDTO.setId(entity.getId());
            return sectionDTO;
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException(ex.getMessage(), ex);
        } catch (DataAccessException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    @Transactional
    @Override
    public void remove(Long id) {
        try {
            sectionRepository.delete(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NoSuchResourceException("no such section for remove!", ex);
        } catch (DataAccessException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SectionWithGeologicalList findById(Long id) {
        if (id == null || id < 1) {
            throw new NoSuchResourceException("no such section to be found!");
        }
        try {
            SectionEntity entity = sectionRepository.findOne(id);
            if (entity == null) {
                throw new NoSuchResourceException("no such section to be found!");
            }
            SectionWithGeologicalList sectionDTO = new SectionWithGeologicalList(entity.getId(), entity.getName());
            if (entity.getGeologicalClasses() != null) {
                sectionDTO.setGeologicalClassList(entity.getGeologicalClasses().stream()
                        .map(g -> new GeologicalResponseDTO(g.getId(), g.getName(), g.getCode())).collect(Collectors.toList()));
            }
            return sectionDTO;
        } catch (DataAccessException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SimplePageResponseDTO<SimpleSectionDTO> findAll(SimplePageRequestDTO pageRequest) {
        PageRequest request = new PageRequest(pageRequest.getPage(), pageRequest.getSize());
        SimplePageResponseDTO<SimpleSectionDTO> simplePageResponse = new SimplePageResponseDTO<>();
        try {
            Page<SectionEntity> page = sectionRepository.findAll(request);
            if (page.getContent().isEmpty()) {
                return simplePageResponse;
            }
            simplePageResponse.setCount((long) page.getTotalElements());
            simplePageResponse.setContent(page.getContent().stream()
                    .map(entity -> new SimpleSectionDTO(entity.getId(), entity.getName())).collect(Collectors.toList()));
            return simplePageResponse;
        } catch (DataAccessException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SimplePageResponseDTO<SimpleSectionDTO> findAllByJob(SectionByJobRequestDTO requestDTO) {
        SimplePageResponseDTO<SimpleSectionDTO> simplePageResponse = new SimplePageResponseDTO<>();
        PageRequest pageRequest = new PageRequest(requestDTO.getPage(), requestDTO.getSize());
        try {
            Page<SectionEntity> page = sectionRepository.findByJobId(requestDTO.getJobId(), pageRequest);
            if (page.getContent().isEmpty()) {
                return simplePageResponse;
            }
            simplePageResponse.setCount((long) page.getTotalElements());
            simplePageResponse.setContent(page.getContent().stream()
                    .map(entity -> new SimpleSectionDTO(entity.getId(), entity.getName())).collect(Collectors.toList()));
            return simplePageResponse;
        } catch (DataAccessException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

}
