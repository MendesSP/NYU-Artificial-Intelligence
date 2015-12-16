function currentState = hillClimbing(state,iterations)
currentState = state;
neighbor=0;
currentValue = heuristicQueens(currentState);
i=0;

while i<iterations
    [s,h,hBoard]  = futureStates(currentState);
    [neighborValue,idx] = min(h);
    if neighborValue > currentValue
        return;
    end
    currentState = s{idx}; %neighborState
    currentValue = heuristicQueens(currentState);
    i = i+1; %for safety
end

