function [s,h,hBoard]  = futureStates(board)

for i=1:8
lastPosition(i) = find(board(:,i)==1);
end

cpyBoard = board;
i=1;

for c=1:8
    for r=1:8
        if r ~= lastPosition(c)
            cpyBoard(:,c) = 0;
            cpyBoard(r,c) = 1;
            s(i) = {cpyBoard};
            h(i) = heuristicQueens(cpyBoard);
            hBoard(r,c) = heuristicQueens(cpyBoard);
            i=i+1;
        else
            hBoard(r,c) = 999;
        end
        cpyBoard = board;
    end
end