package com.epam.rd.autocode.thread;

public class Worker implements Runnable {
	private final int numberOfIterations;
	private final int pause;
	private final Counter counter;
	public Worker(int numberOfIterations, int pause, Counter counter) {
		this.numberOfIterations = numberOfIterations;
		this.pause = pause;
		this.counter = counter;
	}
	@Override
	public void run() {
		for (int i = 0; i < numberOfIterations; i++) {
			boolean equal = counter.getValue1() == counter.getValue2();
			System.out.println(equal + " " + counter.getValue1() + " " + counter.getValue2());
			counter.incrementValue1();
			try {
				Thread.sleep(pause);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			counter.incrementValue2();
		}
	}
}