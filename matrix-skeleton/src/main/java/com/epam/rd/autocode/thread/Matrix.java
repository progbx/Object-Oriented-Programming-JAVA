package com.epam.rd.autocode.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Matrix {
	private static final int MIN_VALUE = 0;
	private static final int MAX_VALUE = 1000;
	public static int[][] matrixGenerator(int m, int n) {
		int[][] res = new int[m][n];
		Random r = new Random();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				res[i][j] = r.nextInt(MAX_VALUE - MIN_VALUE) + MIN_VALUE;
			}
		}
		return res;
	}

	public static String oneThreadSearch(int[][] ar, int pause) throws InterruptedException {
		int max = ar[0][0];
		long time = System.currentTimeMillis();
		for (int[] ints : ar) {
			for (int anInt : ints) {
				Thread.sleep(pause);
				if (anInt > max) {
					max = anInt;
				}
			}
		}
		time = System.currentTimeMillis() - time;
		return max + " " + time;
	}

	public static String multipleThreadSearch(int[][] ar, int pause) throws InterruptedException, ExecutionException {
		int max = ar[0][0];
		long time = System.currentTimeMillis();
		List<Callable<Integer>> tasks = getCallables(ar, pause);
		ExecutorService executor = Executors.newFixedThreadPool(ar.length);
		List<Future<Integer>> results = executor.invokeAll(tasks);
		for (Future<Integer> result : results) {
			int rowMax = result.get();
			if (rowMax > max) {
				max = rowMax;
			}
		}
		executor.shutdown();
		time = System.currentTimeMillis() - time;
		return max + " " + time;
	}

	private static List<Callable<Integer>> getCallables(int[][] ar, int pause) {
		List<Callable<Integer>> tasks = new ArrayList<>();
		for (int i = 0; i < ar.length; i++) {
			int finalI = i;
			Callable<Integer> task = new Callable<>() {
				private final int[] row = ar[finalI];
				@Override
				public Integer call() throws Exception {
					int max = row[0];
					for (int k : row) {
						Thread.sleep(pause);
						if (k > max) {
							max = k;
						}
					}
					return max;
				}
			};
			tasks.add(task);
		}
		return tasks;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		int[][] ar = matrixGenerator(4, 50);
		System.out.println(oneThreadSearch(ar, 1));
		System.out.println(multipleThreadSearch(ar, 1));
	}
}