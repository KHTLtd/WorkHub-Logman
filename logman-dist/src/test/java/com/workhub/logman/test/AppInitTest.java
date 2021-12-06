package com.workhub.logman.test;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.osjava.sj.loader.JndiLoader;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.workhub.logman.spring.config.app.SpringAppConfig;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

class AppInitTest {

	@Test
	@Ignore
	public void init() throws NamingException {
		final DataSource logDs0 = (new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.setName("testDb0;MODE=PostgreSQL")
				.addScript("classpath:sql/createDatabase.sql")
		).build();
		final DataSource logDs1 = (new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.setName("testDb1;MODE=PostgreSQL")
				.addScript("classpath:sql/createDatabase.sql")
		).build();
		final DataSource logDs2 = (new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.setName("testDb2;MODE=PostgreSQL")
				.addScript("classpath:sql/createDatabase.sql")
		).build();

		final Properties jndi = new Properties();
		jndi.put("jdbc/logman-shard-0", logDs0);
		jndi.put("jdbc/logman-shard-1", logDs1);
		jndi.put("jdbc/logman-shard-2", logDs2);

		initContextBuilder(jndi);

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringAppConfig.class);

		Object bean = context.getBean("logmanDs");
		Assertions.assertNotNull(bean);

		context.close();

	}

	private void initContextBuilder(Properties jndi) throws NamingException {
		final InitialContext context = new InitialContext(jndi);
		final JndiLoader loader = new JndiLoader(context.getEnvironment());
		loader.load(jndi, context);
	}

}
