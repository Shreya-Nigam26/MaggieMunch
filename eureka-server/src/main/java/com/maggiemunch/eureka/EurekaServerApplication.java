package com.maggiemunch.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Server - Service Discovery Server for MaggieMunch Microservices
 *
 * This is the central service registry where all microservices register themselves
 * and discover other services. It runs on port 8761 and provides a dashboard for monitoring.
 *
 * Key Features:
 * - Service Registration: Microservices register their instances
 * - Service Discovery: Services can find and communicate with each other
 * - Health Monitoring: Tracks service health and availability
 * - Load Balancing: Supports client-side load balancing
 * - Dashboard: Web UI at http://localhost:8761 for monitoring
 *
 * Note: Using Java 17 due to environment constraints (JDK 17 installed)
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
		System.out.println("Eureka Server is running on http://localhost:8761");
		System.out.println("Eureka Dashboard: http://localhost:8761");
	}

}
