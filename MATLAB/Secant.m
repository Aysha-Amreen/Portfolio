function Secant(arg1)
format long

% x_k = x_k-1 - f(x_k-1) * ((x_k-1 - x_k-2) / (f(x_k-1) - f(x_k-2))
% where k >= 2
% f(x) = -1 - x + x^6

n = str2double(arg1);
x = 2; % x_0 = 2
y = 1; % x_1 = 1
f_x = -1 - x + x^6; % f(x_0) 
f_y = -1 - y + y^6; % f(x_1)
fprintf('\nx_0 = %.8f,  f(x_0) = %.8f\n', x, f_x);
fprintf('\nx_1 = %.8f,  f(x_1) = %.8f\n', y, f_y);

for k = 2 : n
    temp = y;
    y = x - (f_x * ((x - y) / (f_x - f_y)));
    x = temp;
    f_x = -1 - x + x^6; % f(x_k-1) 
    f_y = -1 - y + y^6; % f(x_k-2)
    fprintf('\nx_%d = %.8f,  f(x_%d) = %.8f\n', k, y, k, f_y);
end

% x_5 = 1.1325
% x_6 = 1.1348
% x_7 = 1.1347
% f(x_5) = -0.022
% f(x_6) = 0.00095
% f(x_7) = -0.00001
