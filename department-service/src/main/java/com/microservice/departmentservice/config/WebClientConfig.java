package com.microservice.departmentservice.config;

import com.microservice.departmentservice.client.EmployeeClient;
import com.microservice.departmentservice.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Configuration
public class WebClientConfig {

    @Autowired
    private LoadBalancedExchangeFilterFunction filterFunction;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter(filterFunction);
    }

    @Bean
    public EmployeeClient employeeClient(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.baseUrl("http://employee-service").build();  // Build WebClient with base URL

        return new EmployeeClient() {
            @Override
            public Flux<Employee> findByDepartment(Long departmentId) {
                return webClient
                        .method(HttpMethod.GET)
                        .uri("/employee/department/{departmentId}", departmentId)  // Use URI template with path variable
                        .retrieve()
                        .bodyToFlux(Employee.class);
            }

            @Override
            public Flux<Employee> findAllEmployees(Long departmentId) {
                return webClient
                        .method(HttpMethod.GET)
                        .uri(uriBuilder -> uriBuilder
                                .path("/employee")
                                .queryParam("departmentId", departmentId)
                                .build())  // Use URI builder for optional request parameters
                        .retrieve()
                        .bodyToFlux(Employee.class);
            }
        };
    }
}
