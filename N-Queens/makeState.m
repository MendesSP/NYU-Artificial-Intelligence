function state = makeState(individual)


d = size(individual,2); %8

board = zeros(8,8);
for c=1:d
    board(individual(1,c),c)=1;
end

state = board;
