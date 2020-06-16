package com.helfa.TradeApi;

import com.zaxxer.hikari.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

  @Value("${spring.datasource.url}")
  private String dbUrl;
  @Value("${spring.datasource.username}")
  private String dbUser;
  @Value("${spring.datasource.password}")
  private String dbPassword;
  @Value("${spring.datasource.driverClassName}")
  private String dbDriver;

  @Bean
  public DataSource dataSource() {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      config.setUsername(dbUser);
      config.setPassword(dbPassword);
      config.setDriverClassName(dbDriver);
      return new HikariDataSource(config);
  }
}