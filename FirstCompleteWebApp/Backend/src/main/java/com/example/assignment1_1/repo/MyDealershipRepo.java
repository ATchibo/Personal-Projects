package com.example.assignment1_1.repo;

import com.example.assignment1_1.domain.AppUser;
import com.example.assignment1_1.domain.Car;
import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.dtos.DealershipByAvgCarPrice;
import com.example.assignment1_1.dtos.DealershipDTO;
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
public class MyDealershipRepo {

    @Autowired
    private EntityManager entityManager;

    public Page<DealershipDTO> findAllCountCars(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DealershipDTO> cq = cb.createQuery(DealershipDTO.class);
        Root<Dealership> root = cq.from(Dealership.class);
        Join<Dealership, Car> joinCar = root.join("cars", JoinType.LEFT);
        Join<Dealership, AppUser> joinUser = root.join("author", JoinType.LEFT);
        cq.multiselect(
                root.get("id"),
                root.get("name"),
                root.get("address"),
                root.get("phone"),
                root.get("email"),
                root.get("website"),
                cb.count(joinCar),
                joinUser.get("id"),
                joinUser.get("username")
        ).groupBy(
                root.get("id"),
                root.get("name"),
                root.get("address"),
                root.get("phone"),
                root.get("email"),
                root.get("website"),
                joinUser.get("id"),
                joinUser.get("username")
        );

        TypedQuery<DealershipDTO> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<DealershipDTO> result = query.getResultList();
        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    public Page<DealershipByAvgCarPrice> findDealershipsByAvgCarPrice(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DealershipByAvgCarPrice> query = cb.createQuery(DealershipByAvgCarPrice.class);
        Root<Dealership> root = query.from(Dealership.class);

        Join<Dealership, Car> carsJoin = root.join("cars", JoinType.LEFT);

        query.select(cb.construct(DealershipByAvgCarPrice.class,
                root.get("id"),
                root.get("name"),
                cb.coalesce(cb.avg(carsJoin.get("price")), cb.literal(0))
        )).groupBy(
                root.get("id"),
                root.get("name")
        );

        TypedQuery<DealershipByAvgCarPrice> query1 = entityManager.createQuery(query);
        query1.setFirstResult((int) pageable.getOffset());
        query1.setMaxResults(pageable.getPageSize());
        List<DealershipByAvgCarPrice> result = query1.getResultList();
        long count = result.size();

        return new PageImpl<>(result, pageable, count);
    }

    public Page<Dealership> findAll(Long id, String name, String address, String phone, String email, String website, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dealership> query = cb.createQuery(Dealership.class);
        Root<Dealership> root = query.from(Dealership.class);

        Predicate predicate = cb.conjunction();

        if (id != null) {
            predicate = cb.and(predicate, cb.equal(root.get("id"), id));
        }

        if (name != null) {
            predicate = cb.and(predicate, cb.like(root.get("name"), "%" + name + "%"));
        }

        if (address != null) {
            predicate = cb.and(predicate, cb.like(root.get("address"), "%" + address + "%"));
        }

        if (phone != null) {
            predicate = cb.and(predicate, cb.like(root.get("phone"), "%" + phone + "%"));
        }

        if (email != null) {
            predicate = cb.and(predicate, cb.like(root.get("email"), "%" + email + "%"));
        }

        if (website != null) {
            predicate = cb.and(predicate, cb.like(root.get("website"), "%" + website + "%"));
        }

        query.where(predicate);

        TypedQuery<Dealership> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(typedQuery.getResultList(), pageable, typedQuery.getResultList().size());
    }

    public Page<Dealership> findAllByName(String name, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dealership> query = cb.createQuery(Dealership.class);
        Root<Dealership> root = query.from(Dealership.class);

        Predicate namePredicate = cb.like(root.get("name"), "%" + name + "%");
        query.where(namePredicate);

        TypedQuery<Dealership> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Dealership> dealerships = typedQuery.getResultList();
        long total = dealerships.size();

        return new PageImpl<>(dealerships, pageable, total);
    }

    @Transactional
    public void deleteAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Dealership> deleteQuery = cb.createCriteriaDelete(Dealership.class);
        Root<Dealership> root = deleteQuery.from(Dealership.class);

        deleteQuery.where(cb.isNotNull(root.get("id")));

        entityManager.createQuery(deleteQuery).executeUpdate();
    }
}
