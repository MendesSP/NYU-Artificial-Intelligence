package controllers.AStar;


import java.util.*;

import controllers.Heuristics.SimpleStateHeuristic;
import controllers.AStar.Agent.Node;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import ontology.Types.WINNER;
import tools.ElapsedCpuTimer;

@SuppressWarnings("unused")
public class Agent extends AbstractPlayer{

	public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
		
	}
	
	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		

		SimpleStateHeuristic heuristic =  new SimpleStateHeuristic(stateObs);
		float avgTime = 10;
		float worstTime = 10;
		float totalTime = 0;
		int numberOfIterations = 0;
		Comparator<Node> comparator = new NodeCostFunction();
		PriorityQueue<Node> queue = new PriorityQueue<Node>(comparator);
		PriorityQueue<Node> closed = new PriorityQueue<Node>(comparator);
	    Node currentNode = null;
		
		queue.add(new Node(null, Types.ACTIONS.ACTION_NIL, stateObs, Double.POSITIVE_INFINITY));
		queue.add(new Node(null, Types.ACTIONS.ACTION_NIL, stateObs, Double.POSITIVE_INFINITY));
		closed.add(new Node(null, Types.ACTIONS.ACTION_NIL,stateObs, Double.POSITIVE_INFINITY));
		ArrayList<Types.ACTIONS> possibleActions = stateObs.getAvailableActions();
		
		while(!queue.isEmpty() && elapsedTimer.remainingTimeMillis() > 2 * avgTime && elapsedTimer.remainingTimeMillis() > worstTime){
        //while(!queue.isEmpty()){
			ElapsedCpuTimer methodTime = new ElapsedCpuTimer();
			
			currentNode = queue.remove();
			
			possibleActions = currentNode.state.getAvailableActions();

			for(int i=0;i<possibleActions.size();i++){
				StateObservation newState = currentNode.state.copy();
				newState.advance(possibleActions.get(i));		
				Node newNode = new Node(currentNode, possibleActions.get(i), newState, Double.POSITIVE_INFINITY );
				newNode.getCost();
				if(newNode.state.getGameWinner() == WINNER.PLAYER_WINS){
					break;
				}
				if(newNode.state.getGameWinner() == WINNER.PLAYER_LOSES){
					continue;
				}
				if (!queue.isEmpty()){
					if (newNode.cost < queue.peek().cost)
						queue.add(newNode);
					else if (newNode.cost < closed.peek().cost )
						queue.add(newNode);
				}
					
			}
			closed.remove();
			closed.add(currentNode);

			numberOfIterations += 1;
			totalTime += methodTime.elapsedMillis();
			avgTime = totalTime / numberOfIterations;

		}

		return currentNode.getAction();
	}
	
	
	
	public class NodeCostFunction implements Comparator<Node>
	{
	    @Override
	    public int compare(Node x, Node y)
	    {
	    	return Double.compare(x.cost,y.cost);

	    }
	}
	
	public class Node {
		public Node parent;
		public Types.ACTIONS action;
		public StateObservation state;
		public double cost;
		
		public Node(Node parent, Types.ACTIONS action, StateObservation state, double cost){
			this.parent = parent;
			this.action = action;
			this.state = state;
			this.cost = cost;
		}
		
		public Types.ACTIONS getAction(){
			if(this.parent == null){
				return action;
			}
			if(this.parent.parent == null){
				return action;
			}
			return parent.action;
		}
		
		public void getCost(){
			double g = deepFromRoot(this);
			if (g==0) g=0.1;
			double h = 10/g;
			this.cost = h+g;
		}
	}
	public int deepFromRoot(Node finalNode){
		int i = 0;
		while(finalNode.parent!=null) {
			finalNode = finalNode.parent;
			i++;
		}
		return i;
	}
}


