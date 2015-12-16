function void = printb( barr, nqueens )
%PRINTB function just plots a chess board, given as an array
% basically it just cycles through the matrix, creating plot 
% points wherever there is a "queen" sitting, ie, there's a 1 on the matrix


% set up some constants and empty result vectors first
bsize=size(barr);
cols=bsize(2);
rows=bsize(1);
hitsx=[];
hitsy=[];
missx=[];
missy=[];



% Idea here is that I loop through the rows and cols of the
% answer matrix, generating two plotting matrixes:
% the HITS matrix will plot * wherever a queen sits
% the MISS matrix will plot 0 everywhere else



for i= 1:rows  %cycle through rows
    for j=1:cols  % and cols
        if barr(i,j)  % if there's a 1 in that position
            hitsy(end+1)=rows-i;
            hitsx(end+1)=j;
        else  % otherwise plot that position as empty
            missy(end+1)=rows-i;
            missx(end+1)=j;
        end
    end
end



%set up a bogus matrix wider than the given board
% just to expand the plotting borders a bit
%  just keeps pieces from getting plotted on the graph axes.
bx=[-1, cols+1];
by=[-1, rows+1];



% let 'er rip
plot(hitsx,hitsy,'r*',missx,missy,'bo',bx,by,'.');



% Finally, give it a title
tstr=['Plot of ' num2str(nqueens) ' queens placed on ' num2str(rows) ' by ' num2str(cols) ' chessboard'];
title(tstr);


end