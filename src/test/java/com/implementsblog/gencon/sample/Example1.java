package com.implementsblog.gencon.sample;

import java.util.List;

/**
 * Created by justinrogers on 7/23/17.
 */
public class Example1 {

    <T> Example1(String s) { }

    private <T extends String> Example1(T s, String s1) { }

    protected <T extends String & List> Example1(T s, String s1, String s2) { }

    public <T, S, U extends String, V> Example1(T s, S s1, U s2, V s3) { }

    Example1() { }

    private Example1(Object o) { }

    protected Example1(Object o, Object o1) { }

    public Example1(Object o, Object o1, Object o2) { }
}
