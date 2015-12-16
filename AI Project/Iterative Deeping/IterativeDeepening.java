import java.util.ArrayList;

/*procedure IDDFS(root)
    for depth from 0 to ∞
        found ← DLS(root, depth)
        if found ≠ null
            return found

procedure DLS(node, depth)
    if depth = 0 and node is a goal
        return node
    else if depth > 0
        foreach child of node
            found ← DLS(child, depth−1)
            if found ≠ null
                return found
    return null*/
public class IterativeDeepening {
	
	public Vertex goal;
	public Vertex found;
	
	public IterativeDeepening()
	{
		found = new Vertex();
	}
	
	public Vertex iterativeDeepening(Vertex root, Vertex goal)
	{	
		int infinity = Integer.MAX_VALUE;
		for (int iterator = 0; iterator < infinity; iterator++) 
		{
			DepthLimitedSearch dls = new DepthLimitedSearch(goal, iterator);
			dls.depthLimitedSearch(root);
			if(dls.goalFounded.leaf != null && dls.goalFounded.leaf.equals(goal.leaf))
			{
				found = dls.goalFounded;
				infinity = 0;
			}
			for (Vertex v : dls.stack) {
				v.visited = false;
			}
		}
		return found;
	}
	
	public static void main (String [] args)
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
		Vertex r = new Vertex("OOO", false, null);
		IterativeDeepening id = new IterativeDeepening();
		
		id.iterativeDeepening(v1,v8);
		System.out.println(id.found.leaf);
	}
}