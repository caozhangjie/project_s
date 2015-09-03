function [x, y, ma] = avginterpol(x0, y0, mats, dis)
    lenx = length(x0);
    leny = length(y0);
    x = splitx(lenx, x0, dis);
    y = splitx(leny, y0, dis);
    ma = ones(lenx * dis, leny * dis);
    for i = 1:1:lenx
        ma(i,:) = splitx(leny, (mats())', dis);
    end
    for i = 1:1:leny * dis
        ma(:,i) = (splitx(lenx, mats(i,:), dis))';
    end
end

function x = splitx(len, x0, dis)
    x = zeros(1,dis * len);
    for i = 1:1:(dis - 1)
         x(i+1:dis:((len - 1) * dis + i + 1)) = x0 + ((x0(2) - x0(1)) / dis);
    end
end