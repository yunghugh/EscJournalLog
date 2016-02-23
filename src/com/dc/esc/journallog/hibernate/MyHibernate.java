package com.dc.esc.journallog.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class MyHibernate {
	private static Log log = LogFactory.getLog(MyHibernate.class);
	private static SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		if (null == sessionFactory) {
			if (log.isDebugEnabled()) {
				log.debug("初始化[Hibernate]");
			}
			final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
					.configure() // configures settings from hibernate.cfg.xml
					.build();
			try {
				sessionFactory = new MetadataSources(registry).buildMetadata()
						.buildSessionFactory();
			} catch (Exception e) {
				e.printStackTrace();
				// The registry would be destroyed by the SessionFactory, but we
				// had trouble building the SessionFactory
				// so destroy it manually.
				StandardServiceRegistryBuilder.destroy(registry);
			}
			return sessionFactory;
		}else{
			return sessionFactory;
		}
	}

}
