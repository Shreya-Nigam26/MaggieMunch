package com.maggiemunch.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway - Central Entry Point for MaggieMunch Microservices
 *
 * This is the API Gateway that acts as a single entry point for all client requests.
 * It routes requests to appropriate microservices using service discovery and dynamic routing.
 *
 * Key Features:
 * - Service Discovery: Registers with Eureka Server for dynamic service discovery
 * - Dynamic Routing: Routes requests based on service names using load balancing (lb://)
 * - Central Entry Point: Single point of entry for all microservices
 * - Request Logging: Logs all incoming and outgoing requests for monitoring
 * - Actuator Integration: Health checks and gateway metrics
 *
 * Routes Configured:
 * - /api/users/** -> user-service
 * - /api/menu/** -> menu-service
 * - /api/orders/** -> order-service
 * - /api/payments/** -> payment-service
 *
 * Port: 8080
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
		System.out.println("API Gateway is running on http://localhost:8080");
		System.out.println("Gateway Routes: /api/users/**, /api/menu/**, /api/orders/**, /api/payments/**");
	}

}
