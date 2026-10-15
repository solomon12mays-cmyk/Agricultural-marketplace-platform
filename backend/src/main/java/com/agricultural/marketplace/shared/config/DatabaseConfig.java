package com.agricultural.marketplace.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database-related configuration.
 *
 * <p>TODO: configure connection pool sizing, slow-query logging and
 * auditable entity listeners once the persistence layer is finalized.</p>
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {
}
