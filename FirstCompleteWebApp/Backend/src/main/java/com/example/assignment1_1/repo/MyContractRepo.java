package com.example.assignment1_1.repo;

import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.domain.ShippingContract;
import com.example.assignment1_1.dtos.ContractDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MyContractRepo {

    private final EntityManager entityManager;

    @Autowired
    public MyContractRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Page<ContractDTO> findAllBy(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ContractDTO> query = cb.createQuery(ContractDTO.class);
        Root<ShippingContract> root = query.from(ShippingContract.class);

        query.select(cb.construct(
                ContractDTO.class,
                root.get("id"),
                root.get("contractDate"),
                root.get("contractYearsDuration"),
                root.get("dealership").get("id"),
                root.get("dealership").get("name"),
                root.get("supplier").get("id"),
                root.get("supplier").get("name"),
                root.get("author").get("id"),
                root.get("author").get("username")
        ));

        TypedQuery<ContractDTO> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<ContractDTO> resultList = typedQuery.getResultList();
        return new PageImpl<>(resultList, pageable, resultList.size());
    }

    @Transactional
    public void deleteAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<ShippingContract> deleteQuery = cb.createCriteriaDelete(ShippingContract.class);
        Root<ShippingContract> root = deleteQuery.from(ShippingContract.class);

        deleteQuery.where(cb.isNotNull(root.get("id")));

        entityManager.createQuery(deleteQuery).executeUpdate();
    }
}
