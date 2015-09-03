function w = interpol(x, d)
    function y = guassian(x1, x2)
        theta = 4;
        temp = (x1 - x2) * (x1 - x2)';
        temp = -1 * temp/(2 * theta^2);
        y = exp(temp);
    end
    siz = size(x);
    line = zeros(1,6);
    lineall = zeros(6,6);
    for i = 1:1:siz(1)
        for j = 1:1:siz(1)
            line(j) = guassian(x(j,:), x(i,:));
        end
        lineall(i, :) = line;
    end
    w = lineall \ d;
end
