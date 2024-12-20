package com.cloudSerenityHotel.config;

import java.io.IOException;
import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;


@Configuration
@ComponentScan(basePackages = {"com.cloudSerenityHotel"})
@EnableAspectJAutoProxy
public class RootAppConfig {
	
	@Bean("entityManagerFactory")
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource) throws IllegalArgumentException, NamingException, IOException {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		
		factoryBean.setDataSource(dataSource);
		
		factoryBean.setPackagesToScan("com.cloudSerenityHotel");
		
		return factoryBean;
	}
}
