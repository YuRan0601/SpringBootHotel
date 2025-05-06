package com.cloudSerenityHotel.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
	private static SessionFactory factory = createFactory();
	
	private static SessionFactory createFactory() {
		StandardServiceRegistry serviceRegistry = 
				new StandardServiceRegistryBuilder()
				.configure("hibernate.cfg.xml")
				.build();
		
		return new MetadataSources(serviceRegistry)
				.buildMetadata()
				.buildSessionFactory();
	};
	
	public static SessionFactory getSessionFactory() {
		return factory;
	}
	
	public static void closeSessionFactory() {
		if (factory != null) {
			factory.close();
		}
	}
}
