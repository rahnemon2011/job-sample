package com.kmv.geological.repository.custom;

import com.kmv.geological.domain.dto.geological.GeologicalFilterRequestDTO;
import com.kmv.geological.domain.dto.geological.GeologicalResponseDTO;
import com.kmv.geological.domain.dto.page.SimplePageResponseDTO;
import com.kmv.geological.domain.entity.GeologicalEntity;
import com.kmv.geological.repository.AbstractRepository;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

/**
 * @author h.mohammadi
 */
@Repository
public class GeologicalRepositoryImpl extends AbstractRepository implements GeologicalRepositoryCustom {

    @Override
    public SimplePageResponseDTO<GeologicalResponseDTO> filter(GeologicalFilterRequestDTO requestDTO) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<GeologicalResponseDTO> query = builder.createQuery(GeologicalResponseDTO.class);
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

        Root<GeologicalEntity> root = query.from(GeologicalEntity.class);
        final List<Predicate> predicates = new ArrayList<>(2);

        //something generic for predicators : https://stackoverflow.com/questions/11138118/really-dynamic-jpa-criteriabuilder
        //adding case-insensitive predicates
        if (requestDTO.getName() != null) {
            predicates.add(builder.like(
                    builder.lower(root.<String>get("name")), "%" + requestDTO.getName().toLowerCase() + "%"));
        }
        if (requestDTO.getCode() != null) {
            predicates.add(builder.like(
                    builder.lower(root.<String>get("code")), "%" + requestDTO.getCode() + "%"));
        }

        query.multiselect(root.get("id"), root.get("name"), root.get("code"));
        countQuery.select(builder.count(countQuery.from(GeologicalEntity.class)));

        if (predicates.size() > 0) {
            query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
            countQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        query.orderBy(builder.desc(root.get("id")));
        List<GeologicalResponseDTO> responseDTOs = getEntityManager().createQuery(query)
                .setFirstResult((requestDTO.getPage()) * requestDTO.getSize()).
                        setMaxResults(requestDTO.getSize()).getResultList();
        Long count = getEntityManager().createQuery(countQuery).getSingleResult();

        return new SimplePageResponseDTO<GeologicalResponseDTO>(responseDTOs, count);
    }

}
