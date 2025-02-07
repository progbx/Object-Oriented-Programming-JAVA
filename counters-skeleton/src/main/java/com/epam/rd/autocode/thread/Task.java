package com.epam.rd.autocode.thread;

public class Task {
	private final Thread[] threads;
	public Task(int numberOfThreads, Runnable worker) {
		this.threads = new Thread[numberOfThreads];
		for (int i = 0; i < numberOfThreads; i++) {
			this.threads[i] = new Thread(worker);
		}
	}
	public void start() {
		for (Thread thread : threads) {
			thread.start();
		}
	}
	public void join() {
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		Task demo = new Task(3, new Worker(5, 100, new Counter()));
		demo.start();
		demo.join();
		System.out.println("~~~");
		demo = new Task(3, new SynchronizedWorker(5, 100, new Counter()));
		demo.start();
		demo.join();
	}
}