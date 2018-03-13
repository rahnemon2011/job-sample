package com.kmv.geological.repository;

import com.kmv.geological.domain.entity.BaseEntity;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * if you want to add a common method between all your repositories, you can add it here.
 *
 * @author h.mohammadi
 */
@NoRepositoryBean
public interface MiddleRepository<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID> {

}
