package com.implementsblog.gencon.sample.example4;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Function;

public class Example4<C extends Collection & Function<C, String>> {
    public <T extends String & Serializable> void method1(T t) { }
    public <T extends String> void method2(Collection<T> t) { }
    public <T> void method4(Collection<? super String> t) { }
}
