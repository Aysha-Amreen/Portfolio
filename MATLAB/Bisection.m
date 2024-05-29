function Bisection(arg1)

a = 1;
b = 2;
n = str2double(arg1);
% f(x) = - 1 - x + x^6

for k = 1 : n
    c = (a + b) / 2;
    f_c = -1 - c + c^6;
    f_a = -1 - a + a^6;
    fprintf('\na_%d = %.8f\n', k, a);
    fprintf('b_%d = %.8f\n', k, b);
    fprintf('c_%d = %.8f\n', k, c);
    fprintf('f(c_%d) = %.8f\n', k, f_c);
    if (f_a*f_c < 0) 
        b = c;
    else
        a = c;
    end    
end

% a_9 = 1.1328
% b_9 = 1.1367 
% c_9 = 1.3477 
% f(c_9) = 0.0004


