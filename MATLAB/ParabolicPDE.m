function ParabolicPDE(arg1, arg2)

%initialize variables
k = str2num(arg2);
m = str2num(arg1);
h = 0.1; 
l = 1;
n = l / h;
t = m * k;
    
% Initialize vectors
x = (0:h:l)';
t = (0:k:t)';
u = zeros(n+1, length(t));
    
% Initial and Boundary conditions
u(:, 1) = sin(pi * x);
u(1, :) = 0;
u(n+1, :) = 0;
    
% Crank-Nicolson method
z = k / (h^2);
A = diag((1 + z) * 2 * ones(n-1, 1)) + diag(-z * ones(n-2, 1), 1) + diag(-z * ones(n-2, 1), -1);
B = diag((1 - z) * 2 * ones(n-1, 1)) + diag(z * ones(n-2, 1), 1) + diag(z * ones(n-2, 1), -1);
    
for j = 2:length(t)
    b = B * u(2:n, j-1);
    b(1) = b(1) + z * u(1, j);
    b(n-1) = b(n-1) + z * u(n+1, j);
        
    % Solve tridiagonal 
    u(2:n, j) = tridiag(A, b);
end
    
fprintf('Running Crank-Nicolson method with h = %f, m = %d, and k = %f.\n', h, m, k);
fprintf('Solving %d tridiagonal linear systems of equations to approximate u(x, t).\n', m);
fprintf('Approximations for u(x, t) are below for t = m * k.\n');

% Display using a loop
time = t(end);
for i = 2:n
    fprintf('u(%.1f, %.8f) = %.8f\n', x(i), time, u(i, end));
end
    
end

function x = tridiag(A, b)
    n = length(b);
    x = zeros(n, 1);
    
    % Forward elimination
    for k = 2:n
        f = A(k, k-1) / A(k-1, k-1);
        A(k, :) = A(k, :) - f * A(k-1, :);
        b(k) = b(k) - f * b(k-1);
    end
    
    % Backward substitution
    x(n) = b(n) / A(n, n);
    for k = n-1:-1:1
        x(k) = (b(k) - A(k, k+1) * x(k+1)) / A(k, k);
    end
end