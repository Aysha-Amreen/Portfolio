#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>

#define BUFFSIZE 120

/**
 * This is the main method that is called when the program is executed.
 * It functions similar to the UNIX cat command.
 * @param argc the number of arguments
 * @param argv the command line arguments
 */

int main(int argc, char* argv[]) {

	int n;
	// creating buffer
	char buffer[BUFFSIZE];

	// case when no command line argument is provided
	if (argc == 1) {
		while ((n = read(STDIN_FILENO, buffer, BUFFSIZE)) > 0) {
			// ensures no. of bytes being read is same no. of bytes being written
			if (write(STDOUT_FILENO, buffer, n) != n) perror("write");
		}
		if (n == -1) perror("read"); // ensure read function returns correctly
	} else {
		// loop to perform cat on every filename passed
		for (int i = 1; i < argc; i++) {

			char* filename = argv[i];
			int fd;

			if (strcmp(filename, "-") == 0) {
				fd = STDIN_FILENO; // reading from input
			} else {
				fd = open(filename, O_RDONLY);
			}

			while ((n = read(fd, buffer, BUFFSIZE)) > 0) {
				if (write(STDOUT_FILENO, buffer, n) != n) perror("write");
			}
			if (n == -1) perror("read");
		} // for
	}

} // main
