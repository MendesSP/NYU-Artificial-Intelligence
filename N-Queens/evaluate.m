function popFit = evaluate(pop)

n = size(pop,1);
d = size(pop,2); %8

for n=1:n
    board = zeros(8,8);
    for c=1:d
        board(pop(n,c),c)=1;
    end
    if heuristicQueens(board)~=0
        popFit(n) = 1/heuristicQueens(board);
    else
        popFit(n) = 99;
    end
        
end