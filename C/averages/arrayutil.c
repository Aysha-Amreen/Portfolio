/**
 * Utility file that defines functions to compute maximum element, mean, and
 * variance of an array and to sort the array.
 */

#include "arrayutil.h"

/**
 * Calculates the maximum element in an array.
 *
 * @param arr integer array.
 * @param length size of array.
 * @return the max element.
 */
int getMax (int arr[], int length) {

	int max = arr[0];

	// if new element is bigger, making it max
	for (int i = 1; i < length; i++) {
		if (arr[i] > max) max = arr[i];
	}

	return max;
} // getMax

/**
 * Calculates the mean of elements in an array.
 *
 * @param arr integer array.
 * @param length size of array.
 * @return the mean of elements.
 */
double getMean (int arr[], int length) {

	double total = 0;

	// calculating total value of elements
	for (int i = 0; i < length; i++) {
		total = total + arr[i];
	}

	double mean = total / (double)length;
	return mean;
} // getMean

/**
 * Calculates the variance of elements in an array.
 *
 * @param arr integer array.
 * @param length size of array.
 * @return the variance of elements.
 */
double getVar (int arr[], int length) {

	double mean = getMean(arr, length);
	double sumSqr = 0;

	// calculating sum of squares of elements
	for (int i = 0; i < length; i++) {
		double sqr = arr[i] * arr[i];
		sumSqr = sumSqr + sqr;
	}

	double var = (sumSqr / (double)length) - (mean * mean);
	return var;
} // getVar

/**
 * Sorts the array using insertion sort algorithm.
 */
void sortArray (int arr[], int length) {

	for (int i = 0; i < length; i++) {
		int key = arr[i];
		int j = i - 1;

		// positioning ith element
		while (j >= 0 && arr[j] > key) {
			arr[j + 1] = arr[j];
			j--;
		}

		arr[j + 1] = key;
	} // for
} // sortArray
