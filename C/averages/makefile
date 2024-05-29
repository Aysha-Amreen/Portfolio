NAME = array-util-files
FLAGS = -Wall -pedantic-errors

run: compile
	./$(NAME)

compile: main.o arrayutil.o
	gcc -o $(NAME) main.o arrayutil.o

main.o: main.c arrayutil.h
	gcc -c $(FLAGS) main.c

arrayutil.o: arrayutil.c arrayutil.h
	gcc -c $(FLAGS) arrayutil.c

clean:
	rm -f $(NAME) *.o
