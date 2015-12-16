function pop = createPop(board)

for i=1:size(board,2)
    pop(i) = find(board(:,i)==1);
end
