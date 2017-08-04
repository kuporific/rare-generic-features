package com.implementsblog.gencon.sample.example3;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Collection;

@MyTypeUse
public class Example3<@MyTypeUse @MyTypeParameter U> implements @MyTypeUse Serializable {

    private final @MyTypeUse Object o = new @MyTypeUse Object();

    public <@MyTypeUse @MyTypeParameter T> void method(Collection<@MyTypeUse /*@MyTypeParameter*/ T> c) {
        @MyTypeUse Example3<Object> objectExample3 = new @MyTypeUse Example3<>();
    }

    public <@MyTypeUse @MyTypeParameter T extends A & B & C> void method(T t) { }
}

interface ExampleInterface3<@MyTypeUse @MyTypeParameter U> {
    public <@MyTypeUse @MyTypeParameter T> void method(Collection<@MyTypeUse /*@MyTypeParameter*/ T> c);
}

@Target(ElementType.TYPE_USE)
@interface MyTypeUse {

}

@Target(ElementType.TYPE_PARAMETER)
@interface MyTypeParameter {

}

@MyTypeUse
interface A { void a(); }

interface B { void b(); }
interface C { void c(); }