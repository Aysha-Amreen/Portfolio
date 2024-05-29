function rungeKuttaOrder4
    format long;

    % Initializing variables
    t = 0; % Start time
    y = 4; % Initial height of water in feet
    h = 0.4; % Step size    

    % Loop until y is approximately 0 by updating the value of y and t 
    % at every step
    while y > 0.000001
        [k1, k2, k3, k4, yNext] = rk4_step(y, h);
        if yNext <= 0
            yNext = 0; % Ensure y does not go negative
        end
        y = yNext;
        t = t + h;
        if y == 0 % Break the loop if y has reached 0
            break;
        end
    end

    % Display the t_empty and ensure y is approximately 0 at this t value
    disp(['t_empty = ', num2str(t), ' seconds.']);
    disp(['y = ', num2str(y), ' feet.']); % To check the final value of y
    % Display the current slope approximations used to obtain this value
    disp(['k1 = ', num2str(k1)]);
    disp(['k2 = ', num2str(k2)]);
    disp(['k3 = ', num2str(k3)]);
    disp(['k4 = ', num2str(k4)]);

end

% function to calculate the k-values used to estimate slope approximations 
% at each step in the while loop
% k1 is the slope at the beginning of the time step
% k1 is used to step halfway through the time step - k2 is an estimate 
% of the slope at the midpoint
% k2 is used to step halfway through the time step - k3 is another 
% estimate of the slope at the midpoint
% k3 is used to step all the way across the time step - k4 is an estimate 
% of the slope at the endpoint (t_n + h)
function [k1, k2, k3, k4, yNext] = rk4_step(y, h)
    k1 = dy_dt(y);
    k2 = dy_dt(y + 0.5*h*k1);
    k3 = dy_dt(y + 0.5*h*k2);
    k4 = dy_dt(y + h*k3);
    yNext = y + (h/6)*(k1 + 2*k2 + 2*k3 + k4); % Calculate next y value
end

% Defining the object function used to calculate the slope approximations
function rate = dy_dt(y)
    % Ensure the calculations are valid
    if y <= 0
        rate = 0; % Stop calculation if y is zero or negative
    else
        sqrtArgument = max(0, 64*y); % Ensure non-negative argument for sqrt
        denominator = 8*y - y^2;
        if abs(denominator) < 1e-6
            rate = 0; % Avoid division by a value close to zero
        else
            rate = -(1/24)^2 * sqrt(sqrtArgument) / denominator;
        end
    end
end
