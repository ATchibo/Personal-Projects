package com.example.assignment1_1.repo;

import com.example.assignment1_1.domain.Employee;
import com.example.assignment1_1.domain.ShippingContract;
import com.example.assignment1_1.domain.Supplier;
import com.example.assignment1_1.dtos.SupplierByNrShippings;
import com.example.assignment1_1.dtos.SupplierDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class MySupplierRepo {

    private final EntityManager entityManager;

    @Autowired
    public MySupplierRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Page<SupplierByNrShippings> findAllByNumberOfShippings(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplierByNrShippings> cq = cb.createQuery(SupplierByNrShippings.class);
        Root<Supplier> supplier = cq.from(Supplier.class);

        Join<Supplier, ShippingContract> shippingContract = supplier.join("shippingContracts", JoinType.LEFT);

        cq.select(cb.construct(SupplierByNrShippings.class,
                supplier.get("id"),
                supplier.get("name"),
                cb.count(shippingContract.get("id"))
        ));
        cq.groupBy(supplier.get("id"));

        TypedQuery<SupplierByNrShippings> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<SupplierByNrShippings> result = query.getResultList();
        long total = getTotalCount(entityManager, Supplier.class);

        return new PageImpl<>(result, pageable, total);
    }

    public Page<SupplierDTO> findAllByName(String name, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplierDTO> query = cb.createQuery(SupplierDTO.class);
        Root<Supplier> supplier = query.from(Supplier.class);

        query.select(cb.construct(SupplierDTO.class,
                supplier.get("id"),
                supplier.get("name"),
                supplier.get("email"),
                supplier.get("phone"),
                cb.count(supplier.get("shippingContracts")),
                supplier.get("author").get("id"),
                supplier.get("author").get("username")
        ));

        query.where(cb.like(supplier.get("name"), "%" + name + "%"));
        query.orderBy(cb.asc(supplier.get("id")));

        TypedQuery<SupplierDTO> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<SupplierDTO> resultList = typedQuery.getResultList();
        long count = getCount(query, supplier);

        return new PageImpl<>(resultList, pageable, count);
    }

    public Page<SupplierDTO> findAllBy(Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplierDTO> query = builder.createQuery(SupplierDTO.class);
        Root<Supplier> supplier = query.from(Supplier.class);
        Join<Supplier, ShippingContract> shippingContract = supplier.join("shippingContracts", JoinType.LEFT);
        query.multiselect(supplier.get("id"), supplier.get("name"), supplier.get("email"), supplier.get("phone"),
                builder.count(shippingContract), supplier.get("author").get("id"), supplier.get("author").get("username"));
        query.groupBy(supplier.get("id"), supplier.get("author").get("id"), supplier.get("author").get("username"));
        TypedQuery<SupplierDTO> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<SupplierDTO> results = typedQuery.getResultList();
        return new PageImpl<>(results, pageable, getTotalCount(entityManager, Supplier.class));
    }


    private long getCount(CriteriaQuery<SupplierDTO> query, Root<Supplier> supplier) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        countQuery.select(cb.count(countQuery.from(Supplier.class)));
        countQuery.where(query.getRestriction());

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private long getTotalCount(EntityManager entityManager, Class<?> entityClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(entityClass)));

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Transactional
    public void deleteAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Supplier> deleteQuery = cb.createCriteriaDelete(Supplier.class);
        Root<Supplier> root = deleteQuery.from(Supplier.class);

        deleteQuery.where(cb.isNotNull(root.get("id")));

        entityManager.createQuery(deleteQuery).executeUpdate();
    }
}
