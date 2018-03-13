package com.kmv.geological.domain.dto.section;

import com.kmv.geological.domain.dto.geologicalclass.GeologicalResponseDTO;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author h.mohammadi
 */
public class SectionWithGeologicalList implements Serializable {

    @Min(1)
    private Long id;

    @NotBlank
    private String name;

    private List<GeologicalResponseDTO> geologicalClassList;

    public SectionWithGeologicalList() {
    }

    public SectionWithGeologicalList(String name) {
        this.name = name;
    }

    public SectionWithGeologicalList(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GeologicalResponseDTO> getGeologicalClassList() {
        return geologicalClassList;
    }

    public void setGeologicalClassList(List<GeologicalResponseDTO> geologicalClassList) {
        this.geologicalClassList = geologicalClassList;
    }

}
