package com.kmv.geological.controller;

import com.kmv.geological.aspect.CheckBindingResult;
import com.kmv.geological.domain.dto.page.SimplePageRequestDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;
import com.kmv.geological.domain.dto.section.SectionWithGeologicalList;
import com.kmv.geological.domain.dto.section.SimpleSectionDTO;
import com.kmv.geological.service.api.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author h.mohammadi
 */
@RestController
@RequestMapping("/sections")
public class SectionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SectionController.class.getName());

    private final SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @CheckBindingResult
    @PostMapping
    public SectionWithGeologicalList createOrEditSecion(@RequestBody @Validated SectionWithGeologicalList sectionDTO, BindingResult bindingResult) {
        LOGGER.info("SECTION ==>  ID: {}, NAME: {} ", sectionDTO.getId(), sectionDTO.getName());
        return sectionService.saveOrUpdate(sectionDTO);
    }

    @CheckBindingResult
    @PostMapping("/details")
    public SectionWithGeologicalList searchById(@RequestBody Long sectionId) {
        return sectionService.findById(sectionId);
    }

    @CheckBindingResult
    @GetMapping
    public SimplePageResponseDTO<SimpleSectionDTO> findAll(@Validated SimplePageRequestDTO pageRequest, BindingResult bindingResult) {
        LOGGER.info("PAGEREQUEST ===> PAGE: {}, SIZE: {}", pageRequest.getPage(), pageRequest.getSize());
        return sectionService.findAll(pageRequest);
    }

    @DeleteMapping
    public void remove(@RequestBody Long id) {
        LOGGER.info("SECTION ID :", id);
        sectionService.remove(id);
    }

}
