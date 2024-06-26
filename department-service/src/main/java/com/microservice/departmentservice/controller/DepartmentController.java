package com.microservice.departmentservice.controller;


import com.microservice.departmentservice.client.EmployeeClient;
import com.microservice.departmentservice.model.Department;
import com.microservice.departmentservice.model.Employee;
import com.microservice.departmentservice.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private EmployeeClient employeeClient;

    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public Department add(@RequestBody Department department) {
        LOGGER.info("Department add: {}", department);
        return repository.addDepartment(department);
    }

    @GetMapping
    public List<Department> findAll() {
        LOGGER.info("Department find");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Department findById(@PathVariable Long id) {
        LOGGER.info("Department find: id={}", id);
        return repository.findById(id);
    }


    @GetMapping(value = "/with-employees", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Department> findAllWithEmployees() {
        LOGGER.info("Fetching all departments with employees");

        List<Department> departments = repository.findAll();
        return Flux.fromIterable(departments)
                .delayElements(Duration.ofSeconds(3))  // Delay each department emission by 1 second
                .flatMap(department -> {
                    Flux<Employee> employeesFlux = employeeClient.findByDepartment(department.getId());

                    return employeesFlux
                            .collectList()
                            .map(employees -> {
                                department.setEmployees(employees);
                                return department;
                            });
                });

    }


}