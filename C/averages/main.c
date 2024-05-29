/**
 * main.c
 *
 * Driver for a program that prompts the user for the length and elements of
 * an integer array, then prints the maximum value, mean, variance, and sorted
 * order of that array.
 */

#include <stdio.h>
#include <stdlib.h>
#include "arrayutil.h"

int main() {

	int length;

	printf("Please enter array size: ");
	// checking size is valid integer
	if (scanf("%d", &length) != 1) {
		printf("Input is not a valid integer.\nTerminating...\n");
		return 0;
	}

	int arr[length];

	printf("Please enter %u integers: ", length);
	// checking each input element is a valid integer
	// defining each array element according to input
	for (int i = 0; i < length; i++) {
		if (scanf("%d", &arr[i])  != 1) {
			printf("Input is not a valid integer.\nTerminating...\n");
			return 0;
		}
	}

	printf("Max = %d\n", getMax(arr, length));
	printf("Mean = %lf\n", getMean(arr, length));
	printf("Variance = %lf\n", getVar(arr, length));

	sortArray(arr, length);
	// printing elements of sorted array using loop
	printf("Sorted Array = [%d", arr[0]);
	for (int i = 1; i < length; i++) {
		printf(", %d", arr[i]);
	}
	printf("]\n");

    return EXIT_SUCCESS;
} // main
