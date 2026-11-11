package com.example.backend;

import java.util.List;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Truncates every table in the public schema after each test, so each test starts with an empty database.
 * Needed because tests run against a real server (RANDOM_PORT), where test-managed rollback is not possible.
 * Tables are discovered at runtime, so new tables and foreign keys need no changes here.
 * If a migration tool (Flyway/Liquibase) is added, exclude its history table below.
 */
public class CleanDatabaseExtension implements AfterEachCallback {

  @Override
  public void afterEach(ExtensionContext context) {
    JdbcTemplate jdbc = SpringExtension.getApplicationContext(context).getBean(JdbcTemplate.class);
    List<String> tables = jdbc.queryForList(
        "SELECT quote_ident(tablename) FROM pg_tables WHERE schemaname = 'public'", String.class);
    if (!tables.isEmpty()) {
      jdbc.execute("TRUNCATE TABLE " + String.join(", ", tables));
    }
  }
}
