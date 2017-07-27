package com.implementsblog.gencon.sample;

/**
 * Created by justinrogers on 7/23/17.
 */
public class Example2<@MyAnnotation A> {
    public <@MyAnnotation B, C> void method1() { }
    public <D, @MyAnnotation E> void method2() { }
}

interface InterfaceExample2<@MyAnnotation F> {
    public <@MyAnnotation G, H> void method1();
    public <I, @MyAnnotation J> void method2();
}
