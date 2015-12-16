function children = crossover(parents,cxPoint,sizePop)

children = zeros(sizePop,8);

for i=1:sizePop
    rnd1 = round((sizePop-1)*rand(1,1) + 1);
    rnd2 = round((sizePop-1)*rand(1,1) + 1);
    children(i,1:cxPoint) = parents(rnd1,1:cxPoint);
    children(i,cxPoint+1:end) = parents(rnd2,cxPoint+1:end);
end