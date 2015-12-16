clear all
clc

board = zeros(8,8);
a = 1;
b = 8;
r = round((b-a).*rand(1,1) + a);

for i=1:8
    board(round((b-a).*rand(1,1) + a),i)=1;
end

% board(5,1)=1;
% board(6,2)=1;
% board(7,3)=1;
% board(4,4)=1;
% board(5,5)=1;
% board(6,6)=1;
% board(7,7)=1;
% board(6,8)=1;

% [s,h,hBoard]  = futureStates(board);
% solutionHC = hillClimbing(board,5);
% hHC = heuristicQueens(solutionHC);
% solutionSA = simulatedAnnealing(board,10,100,2);
% hSA = heuristicQueens(solutionSA);
% solutionLB = localBeam(5,5,0);
% hLB = heuristicQueens(solutionLB);
%printb(solutionLB,8);

 solutionGA = geneticAlgorithm(5,100,0,0.9);
 [hGA,v] = heuristicQueens(solutionGA);

    




