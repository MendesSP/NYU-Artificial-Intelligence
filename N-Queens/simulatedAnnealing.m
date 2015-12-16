function currentState = simulatedAnnealing(state,scheduled,iterations,type)
neighbor=0;
currentState = state;
currentValue = heuristicQueens(currentState);
i=0;
T=100;

if type==1 %maximization
    while i<iterations
        T = T - scheduled;
        if T == 0
            return
        end
        [s,h,hBoard]  = futureStates(currentState);
        nextState = s{round((length(s)-1).*rand(1,1) + 1)};
        nextValue = heuristicQueens(nextState);
        deltaE =  nextValue - currentValue;
        if deltaE > 0
            currentState = nextState;
        else
            if exp(deltaE/T) > ((1-0)*rand(1,1) + 0);
                currentState = nextState;
            end
        end
        currentValue = heuristicQueens(currentState);
        i = i+1; %for safety
    end
end

if type==2 %minimization problem
    while i<iterations
        T = T - scheduled;
        if T == 0
            return
        end
        [s,h,hBoard]  = futureStates(currentState);
        nextState = s{round((length(s)-1).*rand(1,1) + 1)};
        nextValue = heuristicQueens(nextState);
        deltaE =  currentValue - nextValue;
        if deltaE > 0
            currentState = nextState;
        else
            if exp(deltaE/T) > ((1-0)*rand(1,1) + 0);
                currentState = nextState;
            end
        end
        currentValue = heuristicQueens(currentState);
        i = i+1; %for safety
    end
end