package com.minicasino.data;

import java.util.Scanner;

public class LogicTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1 - true
        System.out.println("test0: ");
        boolean test0 = scanner.nextInt() == 1;
        System.out.println("test1: ");
        boolean test1 = scanner.nextInt() == 1;
        System.out.println("test2: ");
        boolean test2 = scanner.nextInt() == 1;

//        if (!test0
//            || !test1
//            || !test2) {
//            System.out.println("ok");
//        } else {
//            System.out.println("nope");
//        }

        if (test0 && test1 && !test2
            || test0 && !test1 && test2
            || !test0 && test1 && test2) {
            System.out.println("ok");
        } else {
            System.out.println("nope");
        }
    }
}
