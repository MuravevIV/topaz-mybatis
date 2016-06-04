package com.ilyamur.topaz.mybatis;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    private static final String HSQLDB_SCHEMA_SCRIPT = "classpath:db/schema.sql";
    private static final String HSQLDB_INITIAL_SCRIPT = "classpath:db/initial.sql";

    @Bean
    @Profile(ApplicationProfile.DEV)
    public DataSource dataSource() {
        return (new EmbeddedDatabaseBuilder())
                .setType(HSQL)
                .addScript(HSQLDB_SCHEMA_SCRIPT)
                .addScript(HSQLDB_INITIAL_SCRIPT)
                .build();
    }
}
