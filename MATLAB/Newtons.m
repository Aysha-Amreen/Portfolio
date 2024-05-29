function [] = Newtons(arg1)

% x_k = x_k-1 - (f(x_k-1) / f'(x_k-1)) 
% where k >= 1
% f(x) = x^2 - 2
% f'(x) = 2x

n = str2double(arg1);
x = 2; % x_0 = 2 initial guess for root of f(x)
f = @(x) x^2 - 2; % anonymous function that returns f(x)
g = @(x) 2 * x; % anonymous function that returns f'(x)
fprintf('\nx_0 = %.8f,  f(x_0) = %.8f\n', x, f(x));

for k = 1 : n
    x = x - (f(x) / g(x));
    fprintf('\nx_%d = %.8f,  f(x_%d) = %.8f\n', k, x, k, f(x));
end

% x_1 = 1.1325
% x_2 = 1.1348
% x_3 = 1.1347
% x_4 = 1.1347
% x_5 = 1.1347