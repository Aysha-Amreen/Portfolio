NAME = fifteen
FLAGS = -Wall -pedantic-errors

compile: $(NAME).c
	gcc -o $(NAME) $(FLAGS) $(NAME).c

run: compile
	./$(NAME) 4

clean:
	rm -f $(NAME)
