function currentState = geneticAlgorithm(sizePop,iterations,goal,mutRate)

i=0;
best=0;

if goal==0
    goal=99;
else
    goal = 1/goal;
end

%initialize population with random
for j=1:sizePop
    board = zeros(8,8);
    for i=1:8
        board(round((8-1).*rand(1,1) + 1),i)=1;
    end
    currentStates{j} = board;
end

%initial value for currentState
[pop,popFit, popProb] = allPop(currentStates);
[value, idx] = max(popFit);
currentState=currentStates{idx};                  %this value will be used if the loop cannot find a better value 


parents = [];

while(i<iterations)                                %while the number of iterations does not exceed the maximum, continue
    parents = selectParents(pop, popProb,sizePop); %select parents randonmly but based on the fitness function
    offspring = crossover(parents,3,sizePop);      %use parents to reproduce and generate by using croosover
    if mutRate > ((1-0)*rand(1,1) + 0)             %if mutRate is small offspring probably will not be mutated
        offspring = mutation(offspring,2);         %if mutRate is large, offspring gets mutated
    end
    newPop = [parents; offspring];                 %new populations has all the individuas, parents and children
    popFit = evaluate(newPop);                     %evaluate all the individuals
    [value, idx] = max(popFit);                    %find the best individual
    if value == goal                               %if the individual is fit enough..
        currentState = makeState(newPop(idx,:));   %get the state and return
        return;
    end
    if value>best                                  %if the best individual is not fit enough but is better than the best so far...
        best = value;                              %change best parameter
        currentState = makeState(newPop(idx,:));   %change the best state found so far, this value will be used if the goal is not reached until i reaches maximum
    end
    pop = newPop;                                  %population is equal the new population
    popProb = popFit/sum(popFit);                  %calculate the prob for the individuals so that the cycle can start again
    i=i+1;                                         %increase the variable i from iterations
end
