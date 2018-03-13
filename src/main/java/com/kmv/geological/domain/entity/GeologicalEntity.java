package com.kmv.geological.domain.entity;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Geological Table
 * <p>
 * Each SectionEntity has multi Geological records that we save them in this Table.
 *
 * @author h.mohammadi
 */
@Entity
@Table(name = "geological")
public class GeologicalEntity extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 128)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 64)
    private String code;

    @JoinColumn(name = "FK_SECTION_ID", foreignKey = @ForeignKey(name = "FK_SECTION_ID"))
    @ManyToOne()
    private SectionEntity section;

    public GeologicalEntity() {
    }

    public GeologicalEntity(final Long id) {
        super(id);
    }

    public GeologicalEntity(Long id, String name, String code) {
        super(id);
        this.name = name;
        this.code = code;
    }

    public GeologicalEntity(String name, String code, SectionEntity sectionEntity) {
        this.name = name;
        this.code = code;
        this.section = sectionEntity;
    }

    public GeologicalEntity(String name, String code) {
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

    public SectionEntity getSection() {
        return section;
    }

    public void setSection(SectionEntity section) {
        this.section = section;
    }
}
