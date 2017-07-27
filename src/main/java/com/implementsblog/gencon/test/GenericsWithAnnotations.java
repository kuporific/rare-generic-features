package com.implementsblog.gencon.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Created by justinrogers on 7/19/17.
 */
public class GenericsWithAnnotations<@MyAnnotation T> {
    public <@MyAnnotation T> GenericsWithAnnotations() {

    }

    public <@MyAnnotation T, @MyAnnotation S extends Collection<T>> void returnSomething() {

    }
}

@Target(ElementType.TYPE_PARAMETER)
@interface MyAnnotation {

}
