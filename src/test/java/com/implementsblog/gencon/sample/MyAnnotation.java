package com.implementsblog.gencon.sample;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_PARAMETER;

/**
 * Created by justinrogers on 7/23/17.
 */
@Target(TYPE_PARAMETER)
public @interface MyAnnotation {
}
