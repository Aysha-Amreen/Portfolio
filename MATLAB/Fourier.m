function Fourier(arg1)
    n = str2num(arg1);
    h = 2 * pi / n;
    x = 0 : h : 2 * pi - h;
    f = h^2 * (18*cos(2*x).^2 - 5*cos(2*x) - 16*sin(2*x).^2); % Equation 2

    f_hat = fft(f); 
    y_hat = zeros(1, n); % placeholder vector
    
    for k = 1:n
        w = exp(-2 * pi * i * (k-1) / n);
        neg_w = exp(2 * pi * i * (k-1) / n);
        denominator = -neg_w + (2 + h^2) - w;
        y_hat(k) = f_hat(k) / denominator;
    end

    y = real(ifft(y_hat));

    plot(x, y, 'black');
    xlim([0 (2*pi)]);
    ylim([-1 3.5]);
    xRightBoundary = 2 * pi - h;
    xlabel(['0 <= x <= ' num2str(xRightBoundary)]);
    ylabel('Approximation of y(x)');
    title(['Discrete Fourier approximation, n = ' num2str(n)]);
    tb = axtoolbar('default'); % Get toolbar handle
    tb.Visible = 'off'; % Turn off toolbar
    graphicCurrentFigureHandle = gcf;
    name = "Fourier_n_" + n + ".jpg";
    fprintf("Saving plot to %s in the current working directory.\n", name);
    exportgraphics(graphicCurrentFigureHandle, name);
end