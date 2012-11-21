package com.smartroute.test;

import org.hibernate.ejb.HibernatePersistence;
import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;

public class AbstractPersistenceTest {
    private EntityManager em;
    private EntityManagerFactory emf;


    @Before
    public void setUp() {
        emf = createEntityManagerFactory();
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    protected Map<String, String> createJpaProperties() {
        Map<String, String> p = new HashMap<String, String>();
        p.put("hibernate.dialect", org.hibernate.dialect.MySQL5InnoDBDialect.class.getName());
        return p;
    }

    protected EntityManager getEntityManager() {
        return em;
    }


    @After
    public void cleanUp() {
        em.getTransaction().rollback();
    }



    protected EntityManagerFactory createEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setPersistenceProviderClass(HibernatePersistence.class);
        emfBean.setJpaPropertyMap(createJpaProperties());
        emfBean.setPersistenceUnitName("test");
        emfBean.setPackagesToScan("com.smartroute.model");
        emfBean.setMappingResources("META-INF/orm-test.xml");
        emfBean.setDataSource(new SingleConnectionDataSource("jdbc:mysql://localhost/LkwSchedulerDB", "TestScheduler", "TestScheduler5", true));
        emfBean.afterPropertiesSet();
        return emfBean.getNativeEntityManagerFactory();        
    }
}
