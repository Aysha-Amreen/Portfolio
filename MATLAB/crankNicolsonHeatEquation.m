function ParabolicPDE(m, k)
    % Crank-Nicolson method to solve the heat equation u_t = u_xx
    % on the domain [0,1] with zero boundary conditions and initial condition
    % u(x, 0) = sin(pi*x). Grid spacing h = 0.1.

    % Spatial discretization
    h = 0.1;
    x = 0:h:1;
    N = length(x) - 2;

    % Time step setup
    t_final = m * k; % Final time based on the number of steps and step size
    num_steps = m;

    % Initial condition
    u = sin(pi * x)';

    % Coefficients for the tridiagonal system
    r = k / (2 * h^2);
    main_diag = (1 + 2*r) * ones(N, 1);
    off_diag = -r * ones(N-1, 1);

    % Matrix A and B for the system AU = B*U_old
    A = diag(main_diag) + diag(off_diag, 1) + diag(off_diag, -1);
    B = diag(1 - 2*r) * ones(N, N) - diag(off_diag, 1) - diag(off_diag, -1);

    fprintf('Running Crank-Nicolson method with h = 0.1, m = %d, and k = %.8f.\n', m, k);
    fprintf('Solving %d tridiagonal linear systems of equations to approximate u(x, t).\n', m);
    fprintf('Approximations for u(x, t) are below for t = %.8f.\n', t_final);

    for step = 1:num_steps
        % Update right-hand side
        U_old = u(2:end-1); % Interior points only
        b = B * U_old;
        % Boundary conditions
        b(1) = b(1) + r * u(1); % u(0, t) = 0
        b(end) = b(end) + r * u(end); % u(1, t) = 0

        % Solve the tridiagonal system
        U_new = tridiagSolve(main_diag, off_diag, off_diag, b);
        u = [0; U_new; 0]; % Reinsert boundary conditions
    end

    % Output results
    for i = 2:N+1
        fprintf('u(%.1f, %.8f) = %.8f\n', x(i), t_final, u(i));
    end
end

function x = tridiagSolve(d, a, c, b)
    % Solves the tridiagonal system Ax = b
    n = length(b);
    % Forward elimination
    for i = 2:n
        w = a(i-1) / d(i-1);
        d(i) = d(i) - w*c(i-1);
        b(i) = b(i) - w*b(i-1);
    end
    % Back substitution
    x = b;
    x(n) = b(n) / d(n);
    for i = n-1:-1:1
        x(i) = (b(i) - c(i)*x(i+1)) / d(i);
    end
end
