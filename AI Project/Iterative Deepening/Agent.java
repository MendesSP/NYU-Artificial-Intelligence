package notbot.tiago.iterative.deepening;
import java.util.ArrayList;
import java.util.Stack;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import ontology.Types.WINNER;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractPlayer{

	public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

    }

	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		// TODO Auto-generated method stub
		Stack<Vertex> stack = new Stack<Vertex>();

		float avgTime = 10;
		float worstTime = 10;
		float totalTime = 0;
		int iterations = 1;
		int treeLimit = 1;

		ArrayList<Types.ACTIONS> nextActions = stateObs.getAvailableActions();
		Vertex current = new Vertex(stateObs,ACTIONS.ACTION_NIL,new ArrayList<Vertex>(), 1);

		stack.push(current);

		while(!stack.isEmpty() && elapsedTimer.remainingTimeMillis() > 2 * avgTime 
				&& elapsedTimer.remainingTimeMillis() > worstTime)
		{
			ElapsedCpuTimer methodTime = new ElapsedCpuTimer();
			
			current = stack.pop();

			if(current.vertexVerification())
			{
				break;
			}
			
			if(current.height <= treeLimit){
				nextActions = current.root.getAvailableActions();
				int actionCount = nextActions.size();
				for (int j = 0; j < actionCount; j++) 
				{
					StateObservation nextState = current.root.copy();
					nextState.advance(nextActions.get(j));	
					Vertex node  = new Vertex(nextState, nextActions.get(j), 
							new ArrayList<Vertex>(), current.height + 1);
					stack.push(node);
				}
			}
			
			if(stack.empty())
			{
				//update treeLimit
				//restart stack
				//restart tree height
				treeLimit = current.height;
				stack.push(new Vertex(stateObs, Types.ACTIONS.ACTION_NIL, new ArrayList<Vertex>(), 1));
				System.out.println("vazio");
			}
			totalTime += methodTime.elapsedMillis();
			avgTime = totalTime / iterations;
			iterations = iterations + 1;
		}
		return current.action;
	}

	class Vertex
	{
		public StateObservation root;
		public Types.ACTIONS action;
		public ArrayList<Vertex> leaves;
		public int height;

		public Vertex(StateObservation leaf, Types.ACTIONS action, 
				ArrayList<Vertex> leaves, int height)
		{
			this.root = leaf;
			this.action = action;
			this.leaves = leaves;
			this.height = height;
		}

		public Vertex(Vertex v) 
		{
			this.root = v.root;
			this.action = v.action;
			this.leaves = v.leaves;
			this.height = 1;
		}

		public Vertex() 
		{
			this.leaves = new ArrayList<Vertex>();
			this.height = 1;
		}
		
		public void addVertex(Vertex v)
		{
			this.leaves.add(v);
			this.height = 1;
		}
		
		public boolean vertexVerification()
		{
			boolean verification = false;
			
			if(this.root.getGameWinner() == WINNER.PLAYER_WINS){
				verification = true;
			}
			if(this.root.getGameWinner() == WINNER.PLAYER_LOSES){
				verification = false;
			}
			
			/*for (Vertex vertex : leaves) {
				if(vertex.root.getGameWinner() == WINNER.PLAYER_WINS){
					verification = true;
				}
				if(vertex.root.getGameWinner() == WINNER.PLAYER_LOSES){
					verification = false;
				}
			}*/
			
			return verification;
		}
		
		public int vertexHeight(Vertex v)
		{
			if(v.hasGeneration())
			{
				v.height = v.height + 1;
				ArrayList<Vertex> vertices = v.expandTheVertex();
				for (int i = 0; i < vertices.size(); i++) 
				{
					v.vertexHeight(vertices.get(i));
				}
			}
			return v.height;
		}
		
		public boolean hasGeneration()
		{
			boolean confirmGeneration = false;
			
			if(this.leaves.size() > 0)
			{
				confirmGeneration = true;
			}
			
			return confirmGeneration;
		}
		
		public ArrayList<Vertex> expandTheVertex()
		{
			ArrayList <Vertex> nodeVertices = new ArrayList<Vertex>();

			if(this.leaves != null && this.leaves.size() > 0)
			{
				for (Vertex vertex : this.leaves) 
				{
					nodeVertices.add(vertex); 
				}
			}

			return nodeVertices;
		}

		/*public Vertex getFirstNonVisited()
		{
			Vertex nodeNonVisited = null;

			if(this.leaves != null && this.leaves.size() > 0)
			{
				for (Vertex vertex : this.leaves) 
				{
					if(vertex.visited == false)
					{
						nodeNonVisited = vertex;
					}
				}	
			}
			return nodeNonVisited;
		}*/

		/*public Vertex getFirstVisited()
		{
			Vertex nodeVisited = null;

			if(this.leaves != null && this.leaves.size() > 0)
			{
				for (Vertex vertex : this.leaves) 
				{
					if(vertex.visited == true)
					{
						nodeVisited = vertex;
					}
				}	
			}
			return nodeVisited;
		}*/

		/*public boolean hasNonVisitedChilds()
		{
			boolean nonVisited = false;

			for (Vertex vertex : leaves) 
			{
				if(vertex.visited == false)
				{
					nonVisited = true;
				}
			}

			return nonVisited;
		}

		public void vertexSwap(Vertex x, Vertex y)
		{
			Vertex temp = new Vertex();
			temp = x;
			x = y;
			y = temp;
		}

		/*public void resetGraphVertex()
		{
			if(this.visited) {this.visited = false;}
			if(this.leaves != null)
			{	
				for (int i = 0; i < leaves.size(); i++) 
				{
					this.leaves.get(i).visited = false;
				}
			}
		}*/

		/*public void recursiveNodeVisit(int iter)
		{
			if(this.leaves != null && iter < this.leaves.size())
			{	
				Vertex n = this.leaves.get(iter);
				n.resetGraphVertex();
			}

			recursiveNodeVisit(iter + 1);
		}*/
	}
}