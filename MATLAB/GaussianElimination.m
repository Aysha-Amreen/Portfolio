function [] = GaussianElimination(arg1, arg2)
% Initialize variables 
A = readmatrix(arg1);
n = size(A, 1); % number of rows 
B = readmatrix(arg2);
M = []; % empty matrix

% Print input %
fprintf("Executing naive Gaussian elimination to solve the linear system AX = B.\n")
fprintf("Input for A =\n")
disp(A)
fprintf("Input for B =\n")
disp(B)

% Forward Elimination
for k = 1:(n - 1)
    for i = (k + 1):n
        M(i, k) = A(i, k) / A(k, k);
        for j = (k + 1):n
            A(i, j) = A(i, j) - (M(1, k) * A(k, j));
        end
        A(i, k) = 0;
        B(i) = B(i) - (M(i, k) * B(k));
    end
end

% Print altered matrices
fprintf("Forward elimination complete.\n")
fprintf("Altered A =\n")
disp(A)
fprintf("Altered B =\n")
disp(B)

% Backward substitution
X = B ./ A;
for i = (n - 1):-1:1
    sum = B(i);
    for j = (i + 1):n
        sum = sum - (A(i, j) * X(j));
    end
    X(i) = sum / A(i, i);
end


% Print solution
fprintf("Backward substitution complete.\n")
fprintf("X =\n")
disp(X)