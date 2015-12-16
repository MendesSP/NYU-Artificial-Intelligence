import java.util.ArrayList;
import java.util.Random;

import controllers.Heuristics.SimpleStateHeuristic;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.WINNER;
import tools.ElapsedCpuTimer;

//pseudocode
/*currentNode = startNode;
loop do
L = NEIGHBORS(currentNode);
nextEval = -INF;
nextNode = NULL;
for all x in L 
if (EVAL(x) > nextEval)
nextNode = x;
nextEval = EVAL(x);
if nextEval <= EVAL(currentNode)
//Return current node since no better neighbors exist
return currentNode;
currentNode = nextNode;*/

/**
 * Created with IntelliJ IDEA.
 * User: ssamot
 * Date: 14/11/13
 * Time: 21:45
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class Agent extends AbstractPlayer {

	SimpleStateHeuristic ssh;
    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

    }
    /**
     *
     * Hill Climbing agent.
     * with random node cost
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
    	
    	ArrayList<Node> queue = new ArrayList<Node>();
    	ArrayList<Node> stateChoosedQueue = new ArrayList<Node>();
    	ssh = new SimpleStateHeuristic(stateObs);
    	queue.add(new Node(null, Types.ACTIONS.ACTION_NIL, stateObs,ssh));
    	
    	float avgTime = 10;
		float worstTime = 10;
		float totalTime = 0;
		int limit = 10;
		int iterLimit = 0;
	
		Node currentNode = null;
		ArrayList<Types.ACTIONS> possibleActions = stateObs.getAvailableActions();
		currentNode = queue.remove(0);
		
		while(elapsedTimer.remainingTimeMillis() > 2 * avgTime 
				&& elapsedTimer.remainingTimeMillis() > worstTime)
		{
			if(currentNode.state != null && currentNode.state.getGameWinner() == WINNER.PLAYER_WINS){
				break;
			}
			
			if(currentNode.state != null && currentNode.state.getGameWinner() == WINNER.PLAYER_LOSES){
				continue;
			}
			
			for (Types.ACTIONS action : possibleActions) 
			{
				if(currentNode.state != null){
					StateObservation newState = currentNode.state.copy();
					newState.advance(action);
					queue.add(new Node(currentNode, action, newState, ssh));
				}
			}
			currentNode = eval(queue);
			queue.clear();
		}
		return currentNode.getAction();
    }
    
    public Node nodeComparison(ArrayList<Node> nodes, Node node)
    {
    	Node n = returnNodeWithMaxValue(nodes);
    	if(n.value > node.value)
    	{
    		return n;
    	}else{
    		return node;
    	}
    }
    
    public Node returnNodeWithMaxValue(ArrayList<Node> nodes)
    {
		return nodes.get(new Random().nextInt(nodes.size()-1));
    }
    
    public Node eval(ArrayList<Node> nodes)
	{
		Node node = new Node(null,null,null,null);
		
		double maxNodeCostInArray = 0;
		
		for (Node n : nodes) 
		{
			if(n.value >= maxNodeCostInArray)
			{
				node = n;
				maxNodeCostInArray = n.value;
			}
		}
		
		return node;	
	}
    
	public class Node {
		public Node parent;
		public Types.ACTIONS action;
		public StateObservation state;
		public double value;
		
		public Node(Node parent, Types.ACTIONS action, StateObservation state, 
				SimpleStateHeuristic ssh){
			this.parent = parent;
			this.action = action;
			this.state = state;
			if(this.state != null){
				this.value = ssh.evaluateState(this.state);
			}
			else
			{
				this.value = 0;
			}
		}
		
		public Types.ACTIONS getAction(){
			if(this.parent == null){
				return action;
			}
			if(this.parent.parent == null){
				return action;
			}
			
			return parent.getAction();
		}
		
	}

}