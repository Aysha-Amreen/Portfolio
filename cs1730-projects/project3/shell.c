#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>

#define BUFFSIZE 4096

/* Retrieve the hostname and make sure that this program is not being run on the main odin server.
 * It must be run on one of the vcf cluster nodes (vcf0 - vcf3).
 */
void check()
{
        char hostname[10];
        gethostname(hostname, 9);
        hostname[9] = '\0';
        if (strcmp(hostname, "csci-odin") == 0) {
                fprintf(stderr, "WARNING: TO MINIMIZE THE RISK OF FORK BOMBING THE ODIN SERVER,\nYOU MUST RUN THIS PROGRAM ON ONE OF THE VCF CLUSTER NODES!!!\n");
                exit(EXIT_FAILURE);
        } // if
} // check

/**
 * This function parses the command arguments to identify and set up input and/or
 * output redirection as specified. It modifies the file descriptors for the
 * standard input and/or output to redirect them to the specified files.
 *
 * @param args The command arguments array.
 * @param i The number of arguments in the array.
 */
void redirectIO(char *args[], int i) {
    int in = -1, out = -1, app = -1; // File descriptors for input, output, and append mode
    for (int j = 0; j < i; ++j) {
        // Check for '<' for input redirection
        if (strcmp(args[j], "<") == 0) {
            args[j] = NULL; // Null-terminate the arguments before the '<'
            in = open(args[j + 1], O_RDONLY); // Open the file for reading
            if (in < 0) {
                perror("open"); // Handle errors in opening file
                exit(EXIT_FAILURE);
            }
            if (dup2(in, STDIN_FILENO) < 0) { // Duplicate the file descriptor to standard input
                perror("dup2"); // Handle errors in duplicating file descriptor
                exit(EXIT_FAILURE);
            }
            close(in); // Close the original file descriptor
        }
        // Check for '>' for output redirection
        else if (strcmp(args[j], ">") == 0) {
            args[j] = NULL; // Null-terminate the arguments before the '>'
            out = open(args[j + 1], O_WRONLY | O_CREAT | O_TRUNC, 0644); // Open the file for writing (create if not exists, truncate if exists)
            if (out < 0) {
                perror("open"); // Handle errors in opening file
                exit(EXIT_FAILURE);
            }
            if (dup2(out, STDOUT_FILENO) < 0) { // Duplicate the file descriptor to standard output
                perror("dup2"); // Handle errors in duplicating file descriptor
                exit(EXIT_FAILURE);
            }
            close(out); // Close the original file descriptor
        }
        // Check for '>>' for output redirection (append mode)
        else if (strcmp(args[j], ">>") == 0) {
            args[j] = NULL; // Null-terminate the arguments before the '>>'
            app = open(args[j + 1], O_WRONLY | O_CREAT | O_APPEND, 0644); // Open the file for writing (create if not exists, append if exists)
            if (app < 0) {
                perror("open"); // Handle errors in opening file
                exit(EXIT_FAILURE);
            }
            if (dup2(app, STDOUT_FILENO) < 0) { // Duplicate the file descriptor to standard output
                perror("dup2"); // Handle errors in duplicating file descriptor
                exit(EXIT_FAILURE);
            }
            close(app); // Close the original file descriptor
        }
    }
} //redirectIO

/**
 * Update the shell prompt with the current working directory.
 * Abbreviates the home directory with '~'.
 * @param *home_dir The home directory.
 */
void updatePrompt(char *home_dir) {
    char cwd[BUFFSIZE]; // Buffer to hold the current working directory.
    if (getcwd(cwd, sizeof(cwd)) == NULL) {
        perror("getcwd");
        exit(EXIT_FAILURE);
    }
    // Abbreviate the home directory with '~'.
    if (strncmp(cwd, home_dir, strlen(home_dir)) == 0) {
        printf("1730sh:~%s$ ", cwd + strlen(home_dir));
    } else {
        printf("1730sh:%s$ ", cwd);
    }
}

int main()
{
	check();
	setbuf(stdout, NULL); // makes printf() unbuffered
	int n;
	char cmd[BUFFSIZE];

	// Project 3 TODO: set the current working directory to the user home directory upon initial launch of the shell
	// You may use getenv("HOME") to retrive the user home directory

	// Set the current working directory to the user's home directory at the start.
    char *home_dir = getenv("HOME");
    if (home_dir != NULL) { // check for errors
        if (chdir(home_dir) != 0) {
            perror("chdir");
            exit(EXIT_FAILURE);
        }
    }

	// inifite loop that repeated prompts the user to enter a command
	while (1) {
		// Project 3 TODO: display the current working directory as part of the prompt

		updatePrompt(home_dir); // Update and display the prompt

        // Read the command from standard input.
        n = read(STDIN_FILENO, cmd, BUFFSIZE);

		// if user enters a non-empty command
		if (n > 1) {
			cmd[n-1] = '\0'; // replaces the final '\n' character with '\0' to make a proper C string


			// Lab 06 TODO: parse/tokenize cmd by space to prepare the
			// command line argument array that is required by execvp().
			// For example, if cmd is "head -n 1 file.txt", then the
			// command line argument array needs to be
			// ["head", "-n", "1", "file.txt", NULL].

			// Parse the command
            char *args[BUFFSIZE];
            char *token = strtok(cmd, " ");
            int i = 0;
			// while loop for subsequent calls to strtok() with Null
			// will continue tokenizing the string from where it left off.
            while (token != NULL) {
                args[i++] = token;
                token = strtok(NULL, " ");
            }
            args[i] = NULL; // Null-terminate the argument list

			// Lab 07 TODO: if the command contains input/output direction operators
			// such as "head -n 1 < input.txt > output.txt", then the command
			// line argument array required by execvp() needs to be
			// ["head", "-n", "1", NULL], while the "< input.txt > output.txt" portion
			// needs to be parsed properly to be used with dup2(2) inside the child process

			// Completed in the function "redirectIO"

			// Lab 06 TODO: if the command is "exit", quit the program

			// Check for "exit" command
            if (strcmp(args[0], "exit") == 0) {
                exit(EXIT_SUCCESS);
            }

			// Project 3 TODO: else if the command is "cd", then use chdir(2) to
			// to support change directory functionalities

			// Special handling for the 'cd' command.

			if (strcmp(args[0], "cd") == 0) {
                char *target_dir = args[1]; // Target directory for cd

                if (target_dir == NULL || strcmp(target_dir, "~") == 0) {
                    target_dir = home_dir; // Default to home directory
                } else if (strncmp(target_dir, "~/", 2) == 0) {
                    // Handle '~/' by concatenating home directory and the rest of the path
                    static char new_target_dir[BUFFSIZE];
                    strcpy(new_target_dir, home_dir);
                    strcat(new_target_dir, target_dir + 1); // Skip the '~'
                    target_dir = new_target_dir;
                }

                if (chdir(target_dir) != 0) {
                    perror("chdir");
                }
                continue; // Skip executing any command, return to prompt.
            }

			// Lab 06 TODO: for all other commands, fork a child process and let
			// the child process execute user-specified command with its options/arguments.
			// NOTE: fork() only needs to be called once. DO NOT CALL fork() more than one time.

			// Fork a child process
            pid_t pid = fork();

				// Lab 07 TODO: inside the child process, use dup2(2) to redirect
				// standard input and output as specified by the user command

				// Lab 06 TODO: inside the child process, invoke execvp().
				// if execvp() returns -1, be sure to use exit(EXIT_FAILURE);
				// to terminate the child process

				// Lab 06 TODO: inside the parent process, wait for the child process
				// You are not required to do anything special with the child's
				// termination status

			if (pid == 0) {
                // Child process
                redirectIO(args, i);
				if (execvp(args[0], args) == -1) {
                    perror("execvp");
                    exit(EXIT_FAILURE);
                }
            } else if (pid > 0) {
                // Parent process
                wait(NULL); // Wait for child process
            } else {
                // Fork failed
                perror("fork");
                exit(EXIT_FAILURE);
            }

		} // if
	} // while

} // main
