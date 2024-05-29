function [] = Cholesky(arg1, arg2)
format long;

% Initialize variables 
A = readmatrix(arg1); % symmetric, positive definite n × n matrix
n = size(A, 1); % number of rows 
b = readmatrix(arg2); % nx1 vector
L = zeros(n, n); % nxn matrices with 0 placeholder
% nx1 vectors with 0 placeholder
z = zeros(n, 1); 
x = zeros(n, 1); 

fprintf("Cholesky factorization, A = L*L_transpose.\n")
L(1,1) = sqrt(A(1,1));
for j = 2:n
    L(j,1) = A(j,1)/L(1,1);
end
for i = 2:(n-1)
    sum_val = 0; % Initialize the sum for each i
    for k = 1:(i-1)
        sum_val = sum_val + (L(i,k))^2; % summation term
    end
    L(i,i) = sqrt(A(i,i) - sum_val);
    for j = (i+1):n
        sum_val = 0; % Initialize the sum for each i,j pair
        for k = 1:(i-1)
            sum_val = sum_val + L(i,k) * L(j,k); % summation term
        end
        L(j,i) = (A(i,j) - sum_val) / L(i,i);
    end    
end
sum_val = 0; % Initialize the sum for n
for k = 1:(n-1)
        sum_val = sum_val + (L(n,k))^2; % summation term
end    
L(n,n) = sqrt(A(n,n) - sum_val);

U = L'; % L_transpose

fprintf("L =\n")
disp(L)
fprintf("L_transpose =\n")
disp(U)

fprintf("Solving L*z = b for z.\n")
% Forward Substitution
for i = 1:n
    sum_val = 0; % Initialize the sum for each i,j pair
    for j = 1:(i-1)
        sum_val = sum_val + L(i,j) * z(j,1); % summation term
    end
    z(i, 1) = (b(i,1) - sum_val) / L(i,i);
end
fprintf("z =\n")
disp(z)

fprintf("Solving L_transpose*x = z for x.\n")
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

