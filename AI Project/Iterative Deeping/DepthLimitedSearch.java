import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

public class DepthLimitedSearch
{
	int maximumSearchDepth;
	int numberOfIterates;
	Vertex goalState, goalFounded;
	ArrayList <Vertex> nodesExpanded;
	Stack<Vertex> stack;
	
	public DepthLimitedSearch(Vertex goalState, int maximumSearchDepth)
	{
		this.stack = new Stack<Vertex>();
		this.goalState = goalState;
		this.goalFounded = new Vertex();
		this.maximumSearchDepth = maximumSearchDepth;
		this.numberOfIterates = 0;
		nodesExpanded = new ArrayList<Vertex>();
	}
	
	public DepthLimitedSearch() {
		// TODO Auto-generated constructor stub
	}
	
	public void setDepthLimitedSearch(Vertex goalState, int maximumSearchDepth)
	{
		this.stack = new Stack<Vertex>();
		this.goalState = goalState;
		this.goalFounded = new Vertex();
		this.maximumSearchDepth = maximumSearchDepth;
		this.numberOfIterates = 0;
		nodesExpanded = new ArrayList<Vertex>();
	}

	public void depthLimitedSearch(Vertex node)
	{	
		if(node.leaf.equals(goalState.leaf))
		{
			goalFounded = node;
		}
		else
		{
			if(numberOfIterates < maximumSearchDepth)
			{
				if(node.visited == false || node.hasNonVisitedChilds())
				{
					node.visited = true;
					if(!stack.contains(node))
					{
						stack.push(node);
						numberOfIterates++;
					}
					this.nodesExpanded = stack.peek().expandTheVertex();
					if(this.nodesExpanded.size() > 0 && this.nodesExpanded != null 
							&& stack.peek().hasNonVisitedChilds())
					{
						Vertex newNode = stack.peek().getFirstNonVisited();
						depthLimitedSearch(newNode);
					}
					else
					{
						if(!stack.isEmpty())
						{
							stack.pop();
							depthLimitedSearch(stack.peek());
						}
					}

				}
				else
				{
					if(!stack.isEmpty())
					{						
						stack.pop();
						depthLimitedSearch(stack.peek());
					}
				}

			}
		}
	}
	
	public static void main(String [] args)
	{
		Vertex v1 = new Vertex("A", false, null);
		Vertex v2 = new Vertex("B", false, null);
		Vertex v3 = new Vertex("C", false, null);
		Vertex v4 = new Vertex("D", false, null);
		Vertex v5 = new Vertex("E", false, null);
		Vertex v6 = new Vertex("F", false, null);
		Vertex v7 = new Vertex("G", false, null);
		Vertex v8 = new Vertex("H", false, null);
		
		ArrayList <Vertex> leavesV1 = new ArrayList<Vertex>();
		leavesV1.add(v7);leavesV1.add(v4);leavesV1.add(v2);
		
		ArrayList <Vertex> leavesV2 = new ArrayList<Vertex>();
		leavesV2.add(v1);leavesV2.add(v5);leavesV2.add(v6);
		
		ArrayList<Vertex> leavesV3 = new ArrayList<Vertex>();
		leavesV3.add(v6); leavesV3.add(v8);
		
		ArrayList<Vertex> leavesV4 = new ArrayList<Vertex>();
		leavesV4.add(v1); leavesV4.add(v6);
		
		ArrayList<Vertex> leavesV5 = new ArrayList<Vertex>();
		leavesV5.add(v2); leavesV5.add(v7);
		
		ArrayList<Vertex> leavesV6 = new ArrayList<Vertex>();
		leavesV6.add(v4); leavesV6.add(v3); leavesV6.add(v2);
		
		ArrayList<Vertex> leavesV7 = new ArrayList<Vertex>();
		leavesV7.add(v1); leavesV7.add(v5);
		
		ArrayList<Vertex> leavesV8 = new ArrayList<Vertex>();
		leavesV8.add(v3);
		
		v1.leaves = leavesV1; v2.leaves = leavesV2; v3.leaves = leavesV3; v4.leaves = leavesV4;
		v5.leaves = leavesV5; v6.leaves = leavesV6; v7.leaves = leavesV7; v8.leaves = leavesV8;
		
		DepthLimitedSearch dls = new DepthLimitedSearch(v8, 4);
		dls.depthLimitedSearch(v1);
		System.out.println(dls.goalFounded.leaf +"\n"+ dls.numberOfIterates +"\n"+ dls.stack.size());
		
		v1.recursiveNodeVisit(0);
		System.out.println();
		
	}
	
}

class Vertex
{
	public String leaf;
	public boolean visited;
	public ArrayList<Vertex> leaves;
	
	public Vertex(String leaf, boolean visited, ArrayList<Vertex> leaves)
	{
		this.leaf = leaf;
		this.visited = visited;
		this.leaves = leaves;
	}
	
	public Vertex(Vertex v) 
	{
		this.leaf = v.leaf;
		this.visited = v.visited;
		this.leaves = v.leaves;
	}
	
	public Vertex() {}

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
	
	public Vertex getFirstNonVisited()
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
	}
	
	public Vertex getFirstVisited()
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
	}
	
	public boolean hasNonVisitedChilds()
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
	
	public void resetGraphVertex()
	{
		if(this.visited) {this.visited = false;}
		if(this.leaves != null)
		{	
			for (int i = 0; i < leaves.size(); i++) 
			{
				this.leaves.get(i).visited = false;
			}
		}
	}
	
	public void recursiveNodeVisit(int iter)
	{
		if(this.leaves != null && iter < this.leaves.size())
		{	
			Vertex n = this.leaves.get(iter);
			n.resetGraphVertex();
		}
		
		recursiveNodeVisit(iter + 1);
	}
}
