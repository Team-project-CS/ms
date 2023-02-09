//package com.gp.q.testcontainers;
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.util.TestPropertyValues;
//import org.springframework.context.ApplicationContextInitializer;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.test.context.ContextConfiguration;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import javax.sql.DataSource;
//
//@EnableAutoConfiguration
//@TestConfiguration
//@ContextConfiguration(initializers = PostgreTestConfig.Initializer.class)
//@Testcontainers
//public class PostgreTestConfig {
//
////    @Bean
////    public PostgreSQLContainer postgreSQLContainer() {
////        final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.8");
////        postgreSQLContainer.start();
////        return postgreSQLContainer;
////    }
//
//    @Bean
//    public DataSource dataSource() {
//        final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.8");
//        postgreSQLContainer.start();
//        DataSource build = DataSourceBuilder.create()
//                .driverClassName(postgreSQLContainer.getDriverClassName())
//                .url(postgreSQLContainer.getJdbcUrl())
//                .username(postgreSQLContainer.getUsername())
//                .password(postgreSQLContainer.getPassword())
//                .build();
//        return build;
//    }
//
//
//    @Container
//    public static GenericContainer rabbit = new GenericContainer("rabbitmq:3-management")
//            .withExposedPorts(5672, 15672);
//
//    public static class Initializer implements
//            ApplicationContextInitializer<ConfigurableApplicationContext> {
//        @Override
//        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//            TestPropertyValues values = TestPropertyValues.of(
//                    "spring.rabbitmq.host=" + rabbit.getContainerIpAddress(),
//                    "spring.rabbitmq.port=" + rabbit.getMappedPort(5672)
//            );
//            values.applyTo(configurableApplicationContext);
//        }
//    }
//
//}
