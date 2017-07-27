package com.implementsblog.gencon.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Map;

/**
 * Created by justinrogers on 7/10/17.
 */
public class Example {
    public <T extends Collection> Example(Map<T, ? super Collection> x) {

    }

    public Example(int i) {

    }

    public <T extends Collection> void val(Map<T, ? super Collection> x) {

    }
}

@Target(value = ElementType.ANNOTATION_TYPE)
@interface AnAnnotation { }