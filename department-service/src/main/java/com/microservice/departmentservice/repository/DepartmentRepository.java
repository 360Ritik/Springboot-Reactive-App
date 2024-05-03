package com.microservice.departmentservice.repository;

import com.microservice.departmentservice.model.Department;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DepartmentRepository {
    private final List<Department> departments = new ArrayList<>();

    {
       Department department = new Department(1001L,"developerr");
        Department department1 = new Department(1002L,"developerr");
        Department department2 = new Department(1003L,"developerr");
        Department department3 = new Department(1004L,"developerr");
        Department department4 = new Department(1005L,"developerr");
        departments.add(department);
        departments.add(department1);
        departments.add(department2);
        departments.add(department3);
        departments.add(department4);

    }

    public Department addDepartment(Department department) {
        departments.add(department);
        return department;
    }

    public Department findById(Long id) {
        return departments.stream()
                .filter(department ->
                        department.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public List<Department> findAll() {
        return departments;
    }

}