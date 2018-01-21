package org.afterschoolcreatives.polaris.java.sql.orm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.afterschoolcreatives.polaris.java.sql.ConnectionFactory;

/**
 * Annotation for database entity.
 *
 * @author Jhon Melvin
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    String name();

    String database();

    ConnectionFactory.Driver driver();
}
