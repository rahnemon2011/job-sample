package com.kmv.geological.controller;

import com.kmv.geological.aspect.CheckBindingResult;
import com.kmv.geological.domain.dto.geological.GeologicalFilterRequestDTO;
import com.kmv.geological.domain.dto.geological.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;
import com.kmv.geological.service.geological.GeologicalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author h.mohammadi
 */
@RestController
@RequestMapping("/geological")
public class GeologicalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeologicalController.class.getName());

    private final GeologicalService geologicalService;

    @Autowired
    public GeologicalController(GeologicalService geologicalService) {
        this.geologicalService = geologicalService;
    }

    @CheckBindingResult
    @GetMapping("/filter")
    public SimplePageResponseDTO<GeologicalResponseDTO> filter(
            @Validated GeologicalFilterRequestDTO requestDTO,
            BindingResult bindingResult) {
        return geologicalService.filter(requestDTO);
    }
}
