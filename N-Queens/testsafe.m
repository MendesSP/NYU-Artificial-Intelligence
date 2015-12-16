function [ safe ] = testsafe( board, row, col )
%TESTSAFE tests to see whether the rows, columns, and diagonals
% of the proposed position are free of queens



safe=true; % if nothing bad happens and changes this value
% then it will be returned.


% Now just test all possibilities for bad placements


if sum(board(row,:)) > 0  % if a queen is already on that row
    safe=false;
    

elseif sum(board(:,col))> 0 % if a queen is already on that col
    safe= false;
    

elseif sum(diag(board,col-row))>0 % if anything is on the left-right diag
    safe= false;
    

else  % this is the tough one.  Checks the right-to-left diagonal
    %  Rotate the board first, then test diag
    % again.  Trick is keeping track of where your position is on rotated board!
    rotated=rot90(board); % rotate the board
    newcol=row;  % calculate where your position of interest is on rotated board
    newrow=(8-col)+1;
    

    % the right-left diagonal has now been turned into a left-right diag
    % now just do the same check as before, on the rotated board
    %
    

    if sum(diag(rotated,newcol-newrow))>0
        safe= false;
    end
    

end

return;

end
