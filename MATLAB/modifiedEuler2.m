function [] = modifiedEuler2(arg1, arg2, arg3, arg4, arg5)
format long

h = str2double(arg2);
n = str2double(arg1);
t0 = str2double(arg3);
x0 = str2double(arg4);
y0 = str2double(arg5);

t(1)=t0;
fx= @(t,x,y) y;
fy= @(t,x,y) 2*y-x;
x(1) = x0; y(1)=y0;
X_0=[t(1) x(1) y(1)]';
fprintf('n=%f,  h=%f\n',n,h)
fprintf('X_0=\n')
disp(X_0)
for i=1:n
 t(i+1)  = t(i)+h;
 x1(i+1) = x(i)+0.5*h*fx(t(i),x(i),y(i));
 y1(i+1) = y(i)+0.5*h*fy(t(i),x(i),y(i));
 x(i+1)  = x(i)+(h)*(fx(t(i)+h/2,x1(i+1),y1(i+1)));
 y(i+1)  = y(i)+(h)*(fy(t(i)+h/2,x1(i+1),y1(i+1)));
 X=[t(i+1) x(i+1) y(i+1)]';
 fprintf('X_%d=\n',i)
 disp(X)
end
end




