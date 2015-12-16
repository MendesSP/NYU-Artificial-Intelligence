function ofsMutated = mutation(offspring,nMutations)

n = size(offspring,1);
d = size(offspring,2);

ofsMutated = offspring;

for i=1:n
    for nm=1:nMutations
        rndIdx = round((d-1)*rand(1,1) + 1);
        rndMutation = round((d-1)*rand(1,1) + 1);
        ofsMutated(i,rndIdx) = rndMutation;
    end
end