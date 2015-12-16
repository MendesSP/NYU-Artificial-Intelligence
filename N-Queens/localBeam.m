function currentState = localBeam(k,iterations,goal)

for j=1:k
    board = zeros(8,8);
    for i=1:8
        board(round((8-1).*rand(1,1) + 1),i)=1;
    end
    currentStates{j} = board;
end

i=0;
a=1;

while i<iterations
    for j=1:k
        [nextStates{j},nextValues{j}] = futureStates(currentStates{j}); %generate new states
        for f=1:length(nextStates{j})
            if nextValues{j}(f)<=goal  %test all the states
                currentState = nextStates{j}{f}; %return if the goal was found
                return;
            else
                allNextStates{a} = nextStates{j}{f};
                allNextValues(a) = nextValues{j}(f);
                a=a+1;
            end
        end
    end
    [values,idx]=sort(allNextValues);
    for j=1:k
        currentStates{j} = allNextStates{idx(j)};
    end
    a=1;
    i=i+1;
end

currentState = allNextStates{idx(1)};
