
function result = calc(w, x1, x)
    siz = size(w);
    line = zeros(1,siz(1));
    function y = guassian(x1, x2)
        theta = 4;
        temp = (x1 - x2) * (x1 - x2)';
        temp = -1 * temp/(2 * theta^2);
        y = exp(temp);
    end    
    for j = 1:1:siz(1)
        line(j) = guassian(x1, x(j,:));   
    end
    result = line * w;
end