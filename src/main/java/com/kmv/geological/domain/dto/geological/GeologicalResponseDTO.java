package com.kmv.geological.domain.dto.geological;

/**
 *
 * @author h.mohammadi
 */
public class GeologicalResponseDTO {

    private Long id;
    private String name;
    private String code;

    public GeologicalResponseDTO(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public GeologicalResponseDTO(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public GeologicalResponseDTO() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "GeologicalResponseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
