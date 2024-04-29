package com.microservice.departmentservice.client;

import com.microservice.departmentservice.model.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;


public interface EmployeeClient {

    @GetMapping("/department/{departmentId}")
    Flux<Employee> findByDepartment(@PathVariable("departmentId") Long departmentId);

    // Example of using request parameters (optional)
    @GetMapping("/employee")
    Flux<Employee> findAllEmployees(@RequestParam("departmentId") Long departmentId);
}
