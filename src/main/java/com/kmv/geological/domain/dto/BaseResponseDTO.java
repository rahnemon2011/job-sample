package com.kmv.geological.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author h.mohammadi
 */
public class BaseResponseDTO implements Serializable {

    private Long id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;

    public BaseResponseDTO() {
    }

    public BaseResponseDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
