package com.example.assignment1_1.repo;

import com.example.assignment1_1.domain.*;
import com.example.assignment1_1.dtos.UserDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MyUserRepo {

    @PersistenceContext
    private EntityManager entityManager;

    public List<UserDto> findAllUsers(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserDto> cq = cb.createQuery(UserDto.class);
        Root<AppUser> root = cq.from(AppUser.class);

        cq.multiselect(root.get("id"), root.get("firstName"), root.get("lastName"),
                root.get("email"), root.get("username"), root.get("role"), root.get("location"))
                .orderBy(cb.asc(root.get("id")));

        TypedQuery<UserDto> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    public Long countContractsForUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<AppUser> root = cq.from(AppUser.class);
        Join<AppUser, ShippingContract> joinContracts = root.join("shippingContracts");

        cq.select(cb.count(joinContracts))
                .where(cb.equal(root.get("id"), userId));

        TypedQuery<Long> query = entityManager.createQuery(cq);
        return query.getSingleResult();
    }

    public Long countDealershipsForUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<AppUser> root = query.from(AppUser.class);
        Join<AppUser, Dealership> dealershipJoin = root.joinList("dealerships");
        query.select(cb.count(dealershipJoin));
        query.where(cb.equal(root.get("id"), userId));
        return entityManager.createQuery(query).getSingleResult();
    }

    public Long countCarsForUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<AppUser> root = cq.from(AppUser.class);
        Join<AppUser, Car> carsJoin = root.join("cars", JoinType.LEFT);
        cq.select(cb.count(carsJoin)).where(cb.equal(root.get("id"), userId));
        TypedQuery<Long> query = entityManager.createQuery(cq);
        return query.getSingleResult();
    }

    public Long countEmployeesForUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<AppUser> root = query.from(AppUser.class);
        Join<AppUser, Employee> join = root.join("employees");
        query.select(cb.count(join)).where(cb.equal(root.get("id"), userId));
        return entityManager.createQuery(query).getSingleResult();
    }

    public Long countSuppliersForUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<AppUser> root = cq.from(AppUser.class);
        Join<AppUser, Supplier> suppliers = root.joinList("suppliers", JoinType.LEFT);
        cq.select(cb.count(suppliers));
        cq.where(cb.equal(root.get("id"), userId));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
