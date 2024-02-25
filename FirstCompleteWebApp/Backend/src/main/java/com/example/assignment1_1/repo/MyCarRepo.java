package com.example.assignment1_1.repo;

import com.example.assignment1_1.domain.Car;
import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.dtos.CarDTO;
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

import java.util.List;

@Repository
public class MyCarRepo {

    private final EntityManager entityManager;

    @Autowired
    public MyCarRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public Page<CarDTO> findByPriceGreaterThan(Integer minPrice, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CarDTO> query = cb.createQuery(CarDTO.class);
        Root<Car> root = query.from(Car.class);

        query.select(cb.construct(CarDTO.class,
                root.get("id"),
                root.get("brand"),
                root.get("model"),
                root.get("year"),
                root.get("color"),
                root.get("price"),
                cb.coalesce(root.get("description"), "None"),
                root.get("dealership").get("id"),
                root.get("dealership").get("name"),
                root.get("author").get("id"),
                root.get("author").get("username")
        ));

        query.where(cb.greaterThan(root.get("price"), minPrice));

        return getCarDTOS(pageable, query);
    }

    private Page<CarDTO> getCarDTOS(Pageable pageable, CriteriaQuery<CarDTO> query) {
        TypedQuery<CarDTO> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<CarDTO> cars = typedQuery.getResultList();
        long count = cars.size();

        return new PageImpl<>(cars, pageable, count);
    }

    public Page<CarDTO> findAllBy(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CarDTO> query = cb.createQuery(CarDTO.class);
        Root<Car> root = query.from(Car.class);

        query.select(cb.construct(CarDTO.class,
                root.get("id"),
                root.get("brand"),
                root.get("model"),
                root.get("year"),
                root.get("color"),
                root.get("price"),
                cb.coalesce(root.get("description"), "None"),
                root.get("dealership").get("id"),
                root.get("dealership").get("name"),
                root.get("author").get("id"),
                root.get("author").get("username")
        ))
        .orderBy(cb.asc(root.get("id")));

        return getCarDTOS(pageable, query);
    }

    @Transactional
    public void deleteAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Car> deleteQuery = cb.createCriteriaDelete(Car.class);
        Root<Car> root = deleteQuery.from(Car.class);

        deleteQuery.where(cb.isNotNull(root.get("id")));

        entityManager.createQuery(deleteQuery).executeUpdate();
    }
}
