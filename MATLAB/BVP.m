function [] = BVP(arg1)
format long;

% Initialize variables 
n = str2num(arg1);
h = (1 - 0)/(n + 1);

fprintf("n = %d\n", n)
fprintf("h = %.14f\n", h)
fprintf("Solving the BVP below as a tridiagonal system AX = b\n")
fprintf("using finite difference methods.\n")
fprintf("x’’ = 2x + t\n")
fprintf("x’(0) = 0, x’(1) = 2\n")

% Central difference formula: x_i'' = (x_(i-1) - 2x_i + x_(i+1))/h^2
% t = 0 + ih = ih
% 2x_i + t = (x_(i-1) - 2x_i + x_(i+1))/h^2
% 2x_i + ih = (x_(i-1) - 2x_i + x_(i+1))/h^2
% Rearrange:
% 2h^2*x_i + ih^3 = x_(i-1) - 2x_i + x_(i+1)
% -x_(i-1) + (2+2h^2)x_i - x_(i+1) = -ih^3

% Forward difference formula: x_i' = (x_(i+1) - x_i)/h
% For i = 0: x_0' = (x_1 - x_0)/h
% 0 = (x_1 - x_0)/h
% x_0 = x_1
% Insert into equation for i = 1
% -x_1 + (2+2h^2)x_1 - x_(1+1) = -h^3

% Backward difference formula: x_i' = (x_i - x_(i-1)/h
% For i = n+1: x_(n+1)' = (x_(n+1) - x_n)/h
% 2 = (x_(n+1) - x_n)/h
% x_(n+1) = 2h + x_n
% Insert into equation for i = n
% -x_(n-1) + (2+2h^2)x_n - (2h + x_n) = -nh^3
% -x_(n-1) + (2+2h^2)x_n - x_n = 2h -nh^3

% Initialize A
A_subd = -1 * ones(n, 1); % coefficient of x_(i-1) term in subdiagonal
A_supd = -1 * ones(n, 1); % coefficient of x_(i+1) term in superdiagonal
q = 2 + 2*h^2; % coefficient in diagonal
A_d = q * ones(n, 1); % diagonal
A_d(1, 1) = q - 1;
A_d(n, 1) = q - 1;

% Initialize B
B = [];
B(1, 1) = -h^3; % from forward diff
B(n, 1) = 2*h -n*h^3; % from backward diff
for i = 2:n-1
    B(i) = -i*h^3;
end

% General tridiagonal algorithm
for i = 2:n
    m = A_subd(i-1, 1)/A_d(i-1, 1); % multiplier
	A_d(i, 1) = A_d(i, 1) - m*A_supd(i-1, 1);
	B(i, 1) = B(i, 1) - m*B(i-1, 1);
end
	
% backwards substitution for X
X = [];
	X(n, 1) = B(n, 1)/A_d(n, 1);
for i = (n-1):-1:1 
	X(i, 1) = (B(i, 1) - A_supd(i, 1)*X(i+1, 1))/A_d(i, 1);
end

fprintf("X =\n")
disp(X)
end