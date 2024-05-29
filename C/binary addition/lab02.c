#include <stdio.h>
#include <stdlib.h>

/**
 * Main method of a program that takes k unsigned integers and appends their
 * binary value left to right and outputs the base 10 value of the resulting
 * binary integer.
 */

int main() {

    unsigned int k;
	unsigned int overallValue;
	unsigned int maxValue = 0;
	unsigned int shiftBy;

	printf("Please enter k: "); // prompting user for k
    scanf("%u", &k);

    // check k is a factor of 32
	if (k != 1 && k != 2 && k != 4 && k != 8 && k != 16 && k != 32) {
        printf("Invalid input for k.\n");
        return EXIT_FAILURE;
    }

    shiftBy = 32/k; // determining the no. of bits for each integer
	// loop to determine max value that fits in available bits
	for (int i = 0; i < shiftBy; i++) {
		int powerOf2 = 1;
		// loop to calculate the next power of 2
		for (int j = 1; j <= i; j++) {
			powerOf2 *= 2;
		}
		// incrementing by powers of 2
		maxValue = maxValue + powerOf2;
	}

	printf("Please enter %u unsigned ints: ", k); // prompting for integers

    // loop to scan and compute each integer
	for (int i = 0; i < k; i++) {
		unsigned int input;
		scanf("%u", &input);
		// checking input fits in available space
		if (input > maxValue || input < 0) {
			printf("The integer %u is an invalid input.\n", input);
			return EXIT_FAILURE;
		}
		// left shifting by number of bits for each integer
		overallValue = overallValue << shiftBy;
		// adding binary value of next int to the right of overall value
		overallValue = overallValue + input;
    }

    printf("Overall Value = %u\n", overallValue);

	return EXIT_SUCCESS;
} // main
