package com.kmv.geological.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author h.mohammadi
 */
public abstract class AbstractRepository {

    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
