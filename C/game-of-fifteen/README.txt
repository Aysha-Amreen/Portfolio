Instructions on how to run:
    1. type the command "make" to compile, and link program code.
    2. once compiled, you can use the command "./fifteen" followed by a single integer as a command-line argument for the game board dimensions to run the program
    3. typing the command "make run" compiles, links, and runs the program with 4 as the dimensions of the game board as default.
    4. clean the directory by using the command "make clean" to remove any object files each time before recompiling.

Questions:

    1. Besides 4x4, what other dimensions does the framework allow?
    A) 3x3, 5x5, 6x6, 7x7, 8x8, 9x9
    2. What data structure is used for representation of the game board?
    A) a 2D array
    3. What function is called to greet the player?
    A) void greet(void)
    4. What functions do you need to implement?
    A) void init(void), void draw(void), int move(int tile), int won(void)
