function [pop,popFit, popProb] = allPop(states)

nBoards = size(states,2);
dimBoards = size(states{1},2);

for n=1:nBoards
    for d=1:dimBoards
    pop(n,d) = find(states{n}(:,d)==1);
    end
    if heuristicQueens(states{n})~=0
        popFit(n,1) = 1/heuristicQueens(states{n});
    else
        popFit(n,1) = 99;
    end
end

popProb = popFit/sum(popFit);
[values,idx] = sort(popProb,'descend');
popFit = popFit(idx);
pop = pop(idx,:);

i=2;
