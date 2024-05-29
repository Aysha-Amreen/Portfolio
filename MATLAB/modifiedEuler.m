function [] = modifiedEuler(arg1, arg2, arg3, arg4, arg5)
format long

% Convert input strings to numerical values
n = str2double(arg1);
h = str2double(arg2);
t0 = str2double(arg3); % initial time
x0 = str2double(arg4); % initial value of x
y0 = str2double(arg5); % initial value of x'

X_0 = [t0; x0; y0];

% Display initial values
fprintf("n = %f, h = %f\n", n, h)
fprintf("X_0=\n")
disp(X_0)

% Initialize arrays 
t = zeros(1, n+1);
x = zeros(1, n+1);
y = zeros(1, n+1);
x1 = zeros(1, n+1);
y1 = zeros(1, n+1);

% Set initial conditions
x(1) = x0; 
y(1) = y0;

% differential equation: x'' = 2x' - x
% x' = y
% x'' = 2y - x
f = @(x,y) 2 * y - x; % anonymous function that returns x''

% Modified Euler's method
for i = 1 : n
    t(i+1)  = t(i) + h;    
    x1(i+1) = x(i) + (h/2) * y(i);
    y1(i+1) = y(i) + (h/2) * f(x(i), y(i));
    x(i+1)  = x(i) + h * y1(i+1);
    y(i+1)  = y(i) + h * f(x1(i+1), y1(i+1));
 
    % Display each iteration
    X = [t(i+1); x(i+1); y(i+1)];
    fprintf('X_%d=\n',i)
    disp(X)

end

end
