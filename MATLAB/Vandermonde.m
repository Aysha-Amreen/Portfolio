function [] = Vandermonde(arg1)
format long

n = str2double(arg1);
x = [-1.5 -1 -0.5 0 1]; 
b = [1.6875; -4.25; -3.9375; -2.25; 0.75]

V = zeros(n, n)

for i = 1 : n
    V(i , 1) = 1;
    k = 1;
    for j = 2 : n 
        V(i, j) = x(i) ^ k;
        k = k + 1;
    end
end

fprintf('V = \n')
disp(V)

a = V\b;
disp(a)
