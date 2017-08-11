package com.implementsblog.gencon.sample.example1;

import java.util.List;
import java.util.Map;

/**
 * Created by justinrogers on 7/23/17.
 */
public class ClassExample1<T> {

    /**
     * Some javadoc.
     * @param s
     * @param <T>
     */
    <T> ClassExample1(String s) { }

    private <T extends String> ClassExample1(T s, String s1) { }

    protected <T extends String & List> ClassExample1(T s, String s1, String s2) { }

    public <T, S, U extends String, V> ClassExample1(T s, S s1, U s2, V s3) { }

    ClassExample1() { }

    private ClassExample1(Object o) { }

    protected ClassExample1(Object o, Object o1) { }

    public ClassExample1(Object o, Object o1, Object o2) { }

    public ClassExample1(Class<T> type) { }
}
