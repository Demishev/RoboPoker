package com.robopoker.dbModel;


import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.logging.Logger;

/**
 * User: Demishev
 * Date: 23.12.13
 * Time: 21:15
 */
public class BeansFactoryFactory {

    private static EntityManager entityManager;

	@Produces
    public EntityManager getUserServiceEntityManager() {
		if(entityManager == null)
        entityManager = Persistence.createEntityManagerFactory("EmbDerby").createEntityManager();
		return entityManager; 
    }

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
