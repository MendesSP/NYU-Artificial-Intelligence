function parents = selectParents(pop,popProb,sizePop)

parents = [];
while size(parents,1)<sizePop
    rnd = round(((length(popProb)-1)*rand(1,1) + 1));
    rndProb = ((0.4-0)*rand(1,1) + 0);
    if popProb(rnd)> rndProb;
        parents = [ parents ; pop(popProb==popProb(rnd),:)];
    end
end

parents = parents(1:sizePop,:);