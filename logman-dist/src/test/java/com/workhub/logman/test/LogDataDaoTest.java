package com.workhub.logman.test;

import com.workhub.logman.dao.impl.LogDataDaoImpl;
import com.workhub.logman.data.LogData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Slf4j
public class LogDataDaoTest {

    private LogDataDaoImpl dao = new LogDataDaoImpl();

    @Test
    public void saveLogs() throws SQLException {
        try {
            dao.saveLogs(Collections.singletonList(getPlaceholderData()));
        } catch (Exception e) {
            log.error(e.toString());
            Assertions.fail();
        }
    }


    public LogData getPlaceholderData() {
        LogData logData = new LogData();
        logData.setStamp(LocalDateTime.now());
        logData.setInsertStamp(LocalDateTime.now().plusMinutes(10));
        logData.setLmAddress("donut");
        logData.setSubAddress("donut");
        logData.setLmHost("donut");
        logData.setSubHost("donut");
        logData.setLogger("donut");
        logData.setDistrSubsystem("donut");
        logData.setDistrVersion("1.0.0");
        logData.setDistrBuildNumber("1");
        logData.setMessage("donut");
        logData.setEx("donut");
        logData.setLogId(UUID.randomUUID());
        return logData;
    }

}

class Config {

    @Bean
    public DataSource ds0() throws NamingException {
        final DataSource logDs0 = (new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("testDb0;MODE=PostgreSQL")
                .addScript("classpath:sql/createDatabase.sql")
        ).build();

        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("jdbc/logman-shard-0");
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(true);
        bean.setDefaultObject(logDs0);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }
    @Bean
    public DataSource ds1() throws NamingException {
        final DataSource logDs0 = (new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("testDb1;MODE=PostgreSQL")
                .addScript("classpath:sql/createDatabase.sql")
        ).build();

        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("jdbc/logman-shard-1");
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(true);
        bean.setDefaultObject(logDs0);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }
    @Bean
    public DataSource ds2() throws NamingException {
        final DataSource logDs0 = (new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("testDb2;MODE=PostgreSQL")
                .addScript("classpath:sql/createDatabase.sql")
        ).build();

        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("jdbc/logman-shard-2");
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(true);
        bean.setDefaultObject(logDs0);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }
    @Bean
    public NamedParameterJdbcOperations namedParameterJdbc() throws NamingException {
        return new NamedParameterJdbcTemplate(ds0());
    }
}

