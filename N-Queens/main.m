%%% THE NQUEENS PROBLEM   %%%

      % Problem Summary:

      %   find a way to place N queens on an 8x8 chessboard 

      %   so that no queen can kill another

     
% First get the number of queens to place.

        nqueens=input('How many queens should I place (0-8): ');      

% create an empty start board

        b=zeros(8,8);     

% Here is where the REAL work is done!

        % the PLACE function goes off and tries to place

        % the N queens in a safe way

        answer= place(b, nqueens);    

% the resulting board is captured in ANSWER.

        % so now just report the output


display('Here is the output matrix!');

        display('See the plotter for graphical display!');

        display(answer);     

% Now plot out the answer by calling a nifty plotter function.

    printb(answer, nqueens);