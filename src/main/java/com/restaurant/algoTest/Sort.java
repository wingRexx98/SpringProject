package com.restaurant.algoTest;

import java.util.Arrays;

public class Sort {
	private static final int[] array = { 50, 12, 90, 88, 30, 11, 53, 91, 76, 80 };

	//bubble sort
	public static String bubbleSort(int[] array) {
		int sort = 0;
		for (int i = 0; i < array.length - 1; i++) {
			for (int j = 0; j < array.length - 1 - i; j++) {
				if (array[j] > array[j + 1]) {
					sort = array[j];
					array[j] = array[j + 1];
					array[j + 1] = sort;
				}
			}
		}
		String bubble = Arrays.toString(array) + " bubbleSort";
		return bubble;
	}

	//insertion sort
	public static String insertSort(int[] array) {
		for (int i = 1; i < array.length; i++) {
			int temp = array[i];
			int j = i - 1;
			for (j = i - 1; j >= 0 && array[j] > temp; j--) {
				// Moves the value greater than temp back by one unit
				array[j + 1] = array[j];
			}
			array[j + 1] = temp;
		}
		String sort = Arrays.toString(array) + " insertSort";
		return sort;
	}

	public static void main(String[] args) {
		System.out.println(bubbleSort(array));
		System.out.println(insertSort(array));
	}
}
