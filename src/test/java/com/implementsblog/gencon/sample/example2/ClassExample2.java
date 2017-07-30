package com.implementsblog.gencon.sample.example2;

import com.implementsblog.gencon.sample.MyAnnotation;

/**
 * Created by justinrogers on 7/23/17.
 */
public class ClassExample2<@MyAnnotation A> {
    public <@MyAnnotation B, C> void method1() { }
    public <@MyAnnotation B, C> void method1(B b, C c) { }
    public <D, @MyAnnotation E> void method2() { }
    public <D, @MyAnnotation E> void method2(D d, E e) { }
    public <F> void method3() { }
    public <F> void method3(F f) { }
    public <G, H> void method4() { }
    public <G, H> void method4(G g, H h) { }
    public void method5() { }
    public void method5(Object o) { }
}

