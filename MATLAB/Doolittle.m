function [] = Doolittle(arg1, arg2)
format long;

% Initialize variables 
A = readmatrix(arg1);
n = size(A, 1); % number of rows 
b = readmatrix(arg2);
L = zeros(n, n); % nxn matrix with 0 placeholder
U = zeros(n, n); % nxn matrix with 0 placeholder
z = zeros(n, 1); % nx1 vector with 0 placeholder
x = zeros(n, 1); % nx1 vector with 0 placeholder

fprintf("Doolittle factorization, A = LU.\n")
for k = 1:n
    L(k, k) = 1; % set diagonals to 1
    for j = k:n
        sum_val = 0; % Initialize the sum for each k,j pair
        for s = 1:(k-1)
            sum_val = sum_val + L(k,s) * U(s,j); % summation term
        end
        U(k,j) = A(k,j) - sum_val;
    end
    for i = (k+1):n
        sum_val = 0; % Initialize the sum for each k,i pair
        for s = 1:(k-1)
            sum_val = sum_val + L(i,s) * U(s,k); % summation term
        end
        L(i,k) = (A(i,k) - sum_val) / U(k,k);
    end
end
fprintf("L =\n")
disp(L)
fprintf("U =\n")
disp(U)

fprintf("Solving Lz = b for z.\n")
% Forward Substitution
z(1, 1) = b(1,1);
for i = 2:n
    sum_val = 0; % Initialize the sum for each i,j pair
    for j = 1:(i-1)
        sum_val = sum_val + L(i,j) * z(j,1); % summation term
    end
    z(i, 1) = b(i,1) - sum_val;
end
fprintf("z =\n")
disp(z)

fprintf("Solving Ux = z for x.\n")
% Backward Substitution
x(n, 1) = z(n,1) / U(n,n);
for i = (n-1):(-1):1
    sum_val = 0; % Initialize the sum for each i,j pair
    for j = (i+1):n
        sum_val = sum_val + U(i,j) * x(j,1); % summation term
    end
    x(i, 1) = (z(i,1) - sum_val) / U(i,i);
end
fprintf("x =\n")
disp(x)

end

