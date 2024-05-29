/**************************************************************************
 * This work originally copyright David J. Malan of Harvard University, or-
 * iginally released under:
 * CC BY-NC-SA 3.0  http://creativecommons.org/licenses/by-nc-sa/3.0/
 * licensing.
 *
 * It has been adapted for use in csci 1730.  Per the share-alike
 * clause of this license, this document is also released under the
 * same license.
 **************************************************************************/
/**
 * fifteen.c
 *
 * Implements Game of Fifteen (generalized to d x d).
 *
 * Usage: fifteen d
 *
 * whereby the board's dimensions are to be d x d,
 * where d must be in [DIM_MIN,DIM_MAX]
 *
 * Note that usleep is obsolete, but it offers more granularity than
 * sleep and is simpler to use than nanosleep; `man usleep` for more.
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

// constants
#define DIM_MIN 3
#define DIM_MAX 9

// board
int board[DIM_MAX][DIM_MAX];

// dimensions
int d;

// prototypes
void clear(void);
void greet(void);
void init(void);
void draw(void);
int move(int tile);
int won(void);

int main(int argc, char* argv[])
{
    // ensure proper usage
    if (argc != 2)
    {
        printf("Usage: fifteen d\n");
        return 1;
    }

    // ensure valid dimensions
    d = atoi(argv[1]);
    if (d < DIM_MIN || d > DIM_MAX)
    {
        printf("Board must be between %i x %i and %i x %i, inclusive.\n",
            DIM_MIN, DIM_MIN, DIM_MAX, DIM_MAX);
        return 2;
    }

    // open log
    FILE* file = fopen("log.txt", "w");
    if (file == NULL)
    {
        return 3;
    }

    // greet user with instructions
    greet();

    // initialize the board
    init();

    // accept moves until game is won
    while (1)
    {
        // clear the screen
        clear();

        // draw the current state of the board
        draw();

        // log the current state of the board (for testing)
        for (int i = 0; i < d; i++)
        {
            for (int j = 0; j < d; j++)
            {
                fprintf(file, "%i", board[i][j]);
                if (j < d - 1)
                {
                    fprintf(file, "|");
                }
            }
            fprintf(file, "\n");
        }
        fflush(file);

        // check for win
        if (won())
        {
            printf("win!\n");
            break;
        }

        // prompt for move
        printf("Tile to move (0 to exit): ");
        int tile;
		scanf("%d", &tile);
		getchar();

        // quit if user inputs 0 (for testing)
        if (tile == 0)
        {
            break;
        }

        // log move (for testing)
        fprintf(file, "%i\n", tile);
        fflush(file);

        // move if possible, else report illegality
        if (!move(tile))
        {
            printf("\nIllegal move.\n");
            usleep(50000);
        }

        // sleep thread for animation's sake
        usleep(50000);
    }

    // close log
    fclose(file);

    // success
    return 0;
}

/**
 * Clears screen using ANSI escape sequences.
 */
void clear(void)
{
    printf("\033[2J");
    printf("\033[%d;%dH", 0, 0);
}

/**
 * Greets player.
 */
void greet(void)
{
    clear();
    printf("WELCOME TO GAME OF FIFTEEN\n");
    usleep(200000);
}

/**
 * Initializes the game's board with tiles numbered 1 through d*d - 1
 * (i.e., fills 2D array with values but does not actually print them).
 */
void init(void)
{
	// first element in board
	int max = (d * d) - 1;
	// isOdd = 0 if even and = 1 if odd
	int isOdd = d % 2;
	// two loops to define 2D array until d dimensions
	for (int i = 0; i < d; i++) {
		for (int j = 0; j < d; j++) {
			board[i][j] = max;
			// swap 1 and 2 if even
			if (isOdd == 0) {
				if (max == 2) {
					board[i][j] = 1;
				} else if (max == 1) {
					board[i][j] = 2;
				}
			}
			max--; // iterates until max = 0 because of for loop params
		}
	}
}

/**
 * Prints the board in its current state.
 */
void draw(void)
{
	// 2 loops traverse 2D array until d dimensions
	for (int i = 0; i < d; i++) {
		for (int j = 0; j < d; j++) {
			// prints blank instead of 0
			if (board[i][j] == 0) {
				printf(" _");
			} else if (board[i][j] < 10) {
				printf(" %i", board[i][j]); // space to make board look uniform
			} else {
				printf("%i", board[i][j]);
			}
			if (j < d - 1) {
				printf("|"); // print vertical borders
			}
		}
		printf("\n");
	}
}

/**
 * If tile borders empty space, moves tile and returns 1 (true), else
 * returns 0 (false).
 */
int move(int tile)
{
	int row = -1, column = -1, i = 0;
	// 2 while loops traverse array until tile is found
	while (i < d && row == -1) {
		int j = 0;
		while (j < d && column == -1) {
			if (board[i][j] == tile) {
				row = i; // store row index
				column = j; // store column index
			}
			j++;
		}
		i++;
	}

	// checks which, if any, adjacent square is blank
	// swaps blank with tile if found
	if (board[row + 1][column] == 0 && (row + 1) < d) {
		board[row + 1][column] = board[row][column];
		board[row][column] = 0;
	} else if (board[row - 1][column] == 0 && (row - 1) >= 0) {
		board[row - 1][column] = board[row][column];
		board[row][column] = 0;
	} else if (board[row][column + 1] == 0 && (column + 1) < d) {
		board[row][column + 1] = board[row][column];
		board[row][column] = 0;
	} else if (board[row][column - 1] == 0 && (column - 1) >= 0) {
		board[row][column - 1] = board[row][column];
		board[row][column] = 0;
	} else {
		return 0; // failed return suggests invalid input
	}

	return 1;
}

/**
 * Returns true if game is won (i.e., board is in winning configuration),
 * else false.
 */
int won(void)
{
	int val = 1;
	// two loops traverse 2D array until d dimensions
	for (int i = 0; i < d; i++) {
		for (int j = 0; j < d; j++) {
			if (i != d - 1 || j!= d - 1) { // accounting for last square (blank)
				if (board[i][j] != val) {
					return 0;
				}
			}
			val++; // increments to max value because of loop params
		}
	}
	return 1;
}
