package com.mycompany.hazelcastdemo;

import java.io.Serializable;

public class Person implements Serializable {

    private static final long serialVersionUID = 1L;  // Tercihen ekle

    private String name;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "'}";
    }
}
