package com.randombank.onboarding;

import com.randombank.onboarding.exceptions.DbOccupiedException;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * This class attempts to simulate a slow legacy database, capable only of 2 requests per second. It unfortunately
 * doesn't quite work, but the principle is this:
 * <ol>
 *     <li>Register a Hibernate Interceptor to run when repository queries happen.</li>
 *     <li>Give the class a static timeOfLastDbQuery field.</li>
 *     <li>When a persist or load query is about to happen, check the time elapsed since timeOfLastDbQuery; if less than
 *     500 ms, abort the query with a {@link DbOccupiedException}.</li>
 * </ol>
 * One of the reasons it doesn't quite work is that some repository operations can trigger back-to-back persist and
 * load, so what feels like a single logical operation actually repeatedly runs into the speed limit here. Of course,
 * even if this worked, we would need to implement some kind of retry functionality everywhere the repository is used.
 */
@Component
public class ThrottleInterceptor implements Interceptor, HibernatePropertiesCustomizer {
    public static Instant timeOfLastDbQuery;

    @Value("${random-bank-onboarding.throttle-db:false}")
    private boolean throttleDb;

    @Override
    public boolean onPersist(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) {
        throttle();
        return Interceptor.super.onPersist(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onLoad(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) {
        throttle();
        return Interceptor.super.onLoad(entity, id, state, propertyNames, types);
    }

    private void throttle() {
        if (throttleDb) {
            if (timeOfLastDbQuery != null && Duration.between(timeOfLastDbQuery, Instant.now()).toMillis() < 500) {
                throw new DbOccupiedException();
            }
            timeOfLastDbQuery = Instant.now();
        }
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", this);
    }
}
