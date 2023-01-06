package com.ox48415a484952;

public class Main {
    public static void main(String[] args) throws Exception {
        Tenar tenar = Tenar.getTenar(new MySyringe());

        TestIfTenarWorks testIfTenarWorks = tenar.inject(TestIfTenarWorks.class);
        testIfTenarWorks.log("This is a test from tester class");
    }
}