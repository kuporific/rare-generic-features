package com.implementsblog.gencon.sample.example2;

import com.implementsblog.gencon.sample.MyAnnotation;

interface InterfaceExample2<@MyAnnotation A> {
    <@MyAnnotation B, C> void method1();
    <@MyAnnotation B, C> void method1(B b, C c);
    <D, @MyAnnotation E> void method2();
    <D, @MyAnnotation E> void method2(D d, E e);
    <F> void method3();
    <F> void method3(F f);
    <G, H> void method4();
    <G, H> void method4(G g, H h);
    void method5();
    void method5(Object o);
}