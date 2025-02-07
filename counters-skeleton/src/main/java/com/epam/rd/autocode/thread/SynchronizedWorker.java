package com.epam.rd.autocode.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedWorker implements Runnable {
	private final int numberOfIterations;
	private final int pause;
	private final Counter counter;
	private final Lock lock;
	public SynchronizedWorker(int numberOfIterations, int pause, Counter counter) {
		this.numberOfIterations = numberOfIterations;
		this.pause = pause;
		this.counter = counter;
		this.lock = new ReentrantLock();
	}
	@Override
	public void run() {
		for (int i = 0; i < numberOfIterations; i++) {
			lock.lock();
			try {
				boolean equal = counter.getValue1() == counter.getValue2();
				System.out.println(equal + " " + counter.getValue1() + " " + counter.getValue2());
				counter.incrementValue1();
				try {
					Thread.sleep(pause);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				counter.incrementValue2();
			} finally {
				lock.unlock();
			}
		}
	}
}