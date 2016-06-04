package com.ilyamur.topaz.mybatis;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import({
        MybatisConfiguration.class,
        TransactionConfiguration.class
})
public class ApplicationConfiguration {
}
