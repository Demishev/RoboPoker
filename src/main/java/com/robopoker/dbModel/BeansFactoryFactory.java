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

    @Produces
    public EntityManager getUserServiceEntityManager() {
        return Persistence.createEntityManagerFactory("EmbDerby").createEntityManager();
    }

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
