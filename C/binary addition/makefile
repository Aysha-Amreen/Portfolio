NAME = lab02
FLAGS = -Wall -pedantic-errors

clean: run
	rm -f $(NAME)

run: compile
	./$(NAME)

compile: $(NAME).c
	gcc -o $(NAME) $(FLAGS) $(NAME).c
