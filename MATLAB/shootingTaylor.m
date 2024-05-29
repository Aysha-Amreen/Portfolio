function shootingTaylor
    % Initial conditions and setup
    x0 = 1; % Initial condition x(0)
    x_target = 2; % Boundary condition x(0.5)
    t0 = 0; % Starting time
    t_final = 0.5; % Ending time
    h = 0.5; % Step size for the entire interval
    
    % Initial guesses for y(0) = x'(0)
    z1 = 1.1;
    z2 = 1.4;
    
    % Calculate phi(z1) and phi(z2) using Taylor's method of order 3
    phi_z1 = phi(z1, h, t0, x0, t_final);
    phi_z2 = phi(z2, h, t0, x0, t_final);
    
    % Display phi(z1) and phi(z2)
    fprintf('phi(z1) = %f\n', phi_z1);
    fprintf('phi(z2) = %f\n', phi_z2);
    
    % Linear interpolation to find z3
    z3 = z1 + (z2 - z1) * (x_target - phi_z1) / (phi_z2 - phi_z1);
    
    % Calculate phi(z3) with the interpolated guess
    phi_z3 = phi(z3, h, t0, x0, t_final);
    
    % Display z3 and phi(z3)
    fprintf('z3 = %f\n', z3);
    fprintf('phi(z3) = %f\n', phi_z3);
    
    % Adjust step size for calculating x(0.25)
    h_new = 0.25; % New step size to calculate x at t=0.25
    x_025 = phi(z3, h_new, t0, x0, t0 + h_new); % Use z3 for this calculation
    
    % Display x(0.25)
    fprintf('x(0.25) ≈ %f\n', x_025);
end

function result = phi(z, h, t0, x0, t_final)
    % Initialize variables for the Taylor method
    y = z; % Initial value for y(0) based on the guess z
    x = x0; % Initial x value
    t = t0; % Starting time
    
    while t < t_final
        % Function values based on the differential equation
        f = y; % x'
        fp = y + sin(t); % x'' = y + sin(t)
        
        % Since we don't need f'' for x'' calculation, proceed with Taylor's approximation
        x_next = x + h*f + (h^2)/2*fp; % Taylor's method of order 2 for approximation
        
        % Updating y for the next iteration based on x''
        y = y + h*fp; % This update essentially uses Euler's method for y
        
        % Update x and time for the next iteration
        x = x_next;
        t = t + h;
    end
    
    % Return the final value of x
    result = x;
end
