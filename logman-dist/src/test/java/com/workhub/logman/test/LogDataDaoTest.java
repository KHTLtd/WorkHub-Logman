package com.workhub.logman.test;

import com.workhub.logman.dao.impl.LogDataDaoImpl;
import com.workhub.logman.data.LogData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.test.context.ContextConfiguration;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.UUID;

//@ContextConfiguration("./context.xml")
public class LogDataDaoTest {
//
//    @Autowired
//    private LogDataDaoImpl dao;

//    @Test
    public void saveLogs() {

//        dao.saveLogs();
    }


    public LogData getPlaceholderData() {
        LogData logData = new LogData();
        logData.setCreateStamp(LocalDateTime.now());
        logData.setInsertStamp(LocalDateTime.now().plusMinutes(10));
        logData.setLmAddress("donut");
        logData.setSubAddress("donut");
        logData.setLmHost("donut");
        logData.setSubHost("donut");
        logData.setLogger("donut");
        logData.setSubsystem("donut");
        logData.setMessage("donut");
        logData.setEx("donut");
        logData.setLogId(UUID.randomUUID());
        return logData;
    }

//}
//@Configuration
//class Config {
//
//    @Bean
//    public DataSource ds0() throws NamingException {
//        final DataSource logDs0 = (new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .setName("testDb0;MODE=PostgreSQL")
//                .addScript("classpath:sql/createDatabase.sql")
//        ).build();
//
//        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
//        bean.setJndiName("jdbc/logman-shard-0");
//        bean.setProxyInterface(DataSource.class);
//        bean.setLookupOnStartup(true);
//        bean.setDefaultObject(logDs0);
//        bean.afterPropertiesSet();
//        return (DataSource) bean.getObject();
//    }
//    @Bean
//    public DataSource ds1() throws NamingException {
//        final DataSource logDs0 = (new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .setName("testDb1;MODE=PostgreSQL")
//                .addScript("classpath:sql/createDatabase.sql")
//        ).build();
//
//        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
//        bean.setJndiName("jdbc/logman-shard-1");
//        bean.setProxyInterface(DataSource.class);
//        bean.setLookupOnStartup(true);
//        bean.setDefaultObject(logDs0);
//        bean.afterPropertiesSet();
//        return (DataSource) bean.getObject();
//    }
//    @Bean
//    public DataSource ds2() throws NamingException {
//        final DataSource logDs0 = (new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .setName("testDb2;MODE=PostgreSQL")
//                .addScript("classpath:sql/createDatabase.sql")
//        ).build();
//
//        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
//        bean.setJndiName("jdbc/logman-shard-2");
//        bean.setProxyInterface(DataSource.class);
//        bean.setLookupOnStartup(true);
//        bean.setDefaultObject(logDs0);
//        bean.afterPropertiesSet();
//        return (DataSource) bean.getObject();
//    }

}

