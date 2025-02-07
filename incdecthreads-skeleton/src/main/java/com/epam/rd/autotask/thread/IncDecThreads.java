package com.epam.rd.autotask.thread;

public class IncDecThreads {
    static final int COUNT = 5000;
    static long value;
    static class Increment extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < COUNT; i++){
                value++;
                System.out.println(this.getName() + " : " + currentThread().getName() + " : " + value);
            }
        }
    }
    static class Decrement implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < COUNT; i++){
                value--;
                System.out.println(Thread.currentThread().getClass() + " : " + Thread.currentThread().getName() + " : " + value);
            }
        }
    }
    public static void main(String[] args) {
        Increment increment = new Increment();
        Decrement decrement = new Decrement();
        Thread thread = new Thread(decrement);
        increment.start();
        thread.start();
    }
}