function [ newboard ] = place( aboard, nqueens)
%PLACE gets in a current board, and number of queens left to place
% plus the row to try placing the next queen.
% it just returns the current board if no more queens left to place
% else it places one queen in the target row, then calls itself 
% recursively to place the next queen, passing in the new board.
% if it can't place a queen in that row, it returns false.

% set up some constants
arrsize=size(aboard);
nrows=arrsize(1);
ncols=arrsize(2);
% I'll return this, unless a successful board is calculated below.
newboard=false;



% if no more queens to place, just return current board
if (nqueens==0) newboard=aboard;
else
    % if still queens to place, then try to place one
    % somewhere on current board
    % cycle through rows and cols, trying each position.
    % if you find a good position, recursively pass that board
    % to yourself to try to place the next queen on it!
    for i=1:nrows
        for j=1:ncols
            if (testsafe(aboard, i, j))  % looks like a promising placement
                tempboard=aboard;
                tempboard(i,j)=1; % go ahead and place a queen there
                % and then call myself with that board, asking to 
                % place the next queen on it.
                tempboard=place(tempboard,nqueens-1);
                
                % When something comes back, see if it succeeded, ie,
                % whether all other queens were placed.
                % if so, return the complete board back as value
                % otherwise it'll keep trying other positions.
                % the "if" test is weird:  just testing to see 
                % whether I got a full board (success) or a 1x2 "false"
                % array (failure).  Matlab weirdness...
                if (size(tempboard)==arrsize) 
                    newboard=tempboard;
                    return;
                end
                
            end % inner if
            
        end % for j
    end %for i
    
end % outer if

end