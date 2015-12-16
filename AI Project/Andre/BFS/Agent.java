package controllers.bfsAndre;

import java.util.*;

import controllers.BreadthFirstSearch.Agent.Node;
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
		

		float avgTime = 10;
		float worstTime = 10;
		float totalTime = 0;
		int numberOfIterations = 0;
		Queue<Node> queue = new LinkedList<Node>();
		Node currentNode = null;
		queue.add(new Node(null, Types.ACTIONS.ACTION_NIL, stateObs));
		ArrayList<Types.ACTIONS> possibleActions = stateObs.getAvailableActions();
		
		while(!queue.isEmpty() && elapsedTimer.remainingTimeMillis() > 2 * avgTime && elapsedTimer.remainingTimeMillis() > worstTime){

			ElapsedCpuTimer methodTime = new ElapsedCpuTimer();
			
			currentNode = queue.remove();
			if(currentNode.state.getGameWinner() == WINNER.PLAYER_WINS){
				break;
			}
			if(currentNode.state.getGameWinner() == WINNER.PLAYER_LOSES){
				continue;
			}
			
			possibleActions = currentNode.state.getAvailableActions();

			for(int i=0;i<possibleActions.size();i++){
				StateObservation newState = currentNode.state.copy();
				newState.advance(possibleActions.get(i));
				queue.add(new Node(currentNode, possibleActions.get(i),newState));
			}
			

			numberOfIterations += 1;
			totalTime += methodTime.elapsedMillis();
			avgTime = totalTime / numberOfIterations;

		}

		return currentNode.getAction();
	}

	public class Node {
		public Node parent;
		public Types.ACTIONS action;
		public StateObservation state;
		
		public Node(Node parent, Types.ACTIONS action, StateObservation state){
			this.parent = parent;
			this.action = action;
			this.state = state;
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
	}	
}

