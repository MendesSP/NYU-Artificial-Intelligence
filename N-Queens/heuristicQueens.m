function [h,v] = heuristicQueens(board)
rotBoard = rot90(board);
h=0;

if sum(sum(board))==8
    v=1;
else
    v=0;
end

for i=1:size(board,1)
    h = h+ sum(nFactorial(sum(board(:,i)))) + sum(nFactorial(sum(board(i,:))))+... %sum of rows and columns
        nFactorial(sum(diag(board,-i))) + nFactorial(sum(diag(board,+i)))+... %sum of normal diagonals
        nFactorial(sum(diag(rotBoard,-i))) + nFactorial(sum(diag(rotBoard,+i))); %sum of inverse diagonals
end
h = h + nFactorial(sum(diag(board))) + nFactorial(sum(diag(rotBoard))); 
