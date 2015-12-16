package q_learning;

import java.util.Random;
import core.game.StateObservation;
import ontology.Types;

public class Q {
	public StateObservation state;
	public Types.ACTIONS action;
	public double value;
	public double scr;
	
	public Q(StateObservation state, 
			Types.ACTIONS action, 
			double value, double scr)
	{
		this.state = state;
		this.action = action;
		this.value = value;
	}
}