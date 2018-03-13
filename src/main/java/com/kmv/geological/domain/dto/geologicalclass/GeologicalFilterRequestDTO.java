package com.kmv.geological.domain.dto.geologicalclass;

import com.kmv.geological.domain.dto.page.SimplePageRequestDTO;

import javax.validation.constraints.Size;

/**
 * @author h.mohammadi
 */
public class GeologicalFilterRequestDTO extends SimplePageRequestDTO {

    @Size(max = 128)
    private String name;

    @Size(max = 64)
    private String code;

    public GeologicalFilterRequestDTO() {
    }

    public GeologicalFilterRequestDTO(String name, String code, int page, int size) {
        super(page, size);
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
