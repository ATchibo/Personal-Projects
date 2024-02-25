package com.example.assignment1_1.repo;

import com.example.assignment1_1.domain.Employee;
import com.example.assignment1_1.domain.ShippingContract;
import com.example.assignment1_1.dtos.EmployeeDTO;
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
public class MyEmployeeRepo {


    private final EntityManager entityManager;

    @Autowired
    public MyEmployeeRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public Page<EmployeeDTO> findAllBy(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeeDTO> cq = cb.createQuery(EmployeeDTO.class);
        Root<Employee> root = cq.from(Employee.class);

        cq.select(cb.construct(EmployeeDTO.class,
                root.get("id"),
                root.get("name"),
                root.get("role"),
                root.get("email"),
                root.get("phone"),
                root.get("salary"),
                root.get("dealership").get("id"),
                root.get("dealership").get("name"),
                root.get("author").get("id"),
                root.get("author").get("username")
        ));

        TypedQuery<EmployeeDTO> query = entityManager.createQuery(cq);

        query.setFirstResult((int)pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<EmployeeDTO> result = query.getResultList();
        long total = result.size();

        return new PageImpl<>(result, pageable, total);
    }

    @Transactional
    public void deleteAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Employee> deleteQuery = cb.createCriteriaDelete(Employee.class);
        Root<Employee> root = deleteQuery.from(Employee.class);

        deleteQuery.where(cb.isNotNull(root.get("id")));

        entityManager.createQuery(deleteQuery).executeUpdate();
    }
}
