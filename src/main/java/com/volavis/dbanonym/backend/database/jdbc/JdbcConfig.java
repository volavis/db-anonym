package com.volavis.dbanonym.backend.database.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Spring configuration for JdbcDao.
 */
@Configuration
public class JdbcConfig {

    /**
     * NamedParameterJdbcTemplate bean.
     * @param settings Connection settings for data source.
     * @return Spring Bean.
     */
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Lazy
    @Qualifier("npJdbcTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(ConnectionSettings settings) {
        return new NamedParameterJdbcTemplate(
                DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(settings.getVersion().getDriverClassName())
                .url(settings.getUrl())
                .username(settings.getUser())
                .password(settings.getPassword())
                .build()
        );
    }
}