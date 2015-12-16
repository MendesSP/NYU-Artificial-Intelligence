package q_learning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import controllers.Heuristics.SimpleStateHeuristic;
import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

public class Agent extends AbstractPlayer{
	
	private String fileName;
	private double[] maxValues;
	private ArrayList<Tuple> tuples;
	private Training_Q training;
	private double q_current, q_old;
	private double alpha, discountFactor;
	double [] weights;
	public Q currentQsa;
	public SimpleStateHeuristic ssh;
	
	public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
		
		fileName = "bigFile.txt";
		maxValues = null;
		q_current = 0.0;
		q_old = 0.0;
		alpha = 0.6;
		discountFactor = 0.4;
		ssh = new SimpleStateHeuristic(stateObs);
		currentQsa = new Q(stateObs, Types.ACTIONS.ACTION_NIL, 0.0, ssh.evaluateState(stateObs));
		tuples = new ArrayList<Tuple>();
		
		tools.IO input = new tools.IO();
		String[] lines = input.readFile(fileName);
		for(int i=0; i<lines.length; i++){
			Tuple t = new Tuple(lines[i]);
			if(maxValues == null){
				maxValues = new double[t.values.size()];
			}
			tuples.add(t);
			for(int j=0; j<t.values.size(); j++){
				if(maxValues[j] < t.values.get(j)){
					maxValues[j] = t.values.get(j);
				}
			}
		}
		
		for(int i=0; i<maxValues.length; i++){
			if(maxValues[i] <= 0){
				maxValues[i] = 1;
			}
		}
		
		for(int i=0; i<tuples.size(); i++)
		{
			tuples.get(i).normalize(maxValues);
		}
		
		training = new Training_Q(tuples);
		weights = training.updateWeights();
		weights = training.normalize(weights);
		
	}
	
	private void analyzeData(ArrayList<Observation>[] observations, Vector2d avatarPosition, Tuple data){
		HashMap<Types.ACTIONS, Integer> numbers = new HashMap<Types.ACTIONS, Integer>();
        double shortestDistance = -1;
        Types.ACTIONS shortestDirection = Types.ACTIONS.ACTION_NIL;
        
        numbers.put(ACTIONS.ACTION_UP, 0);
        numbers.put(ACTIONS.ACTION_DOWN, 0);
        numbers.put(ACTIONS.ACTION_LEFT, 0);
        numbers.put(ACTIONS.ACTION_RIGHT, 0);
        
        if(observations != null){
	        for(int t=0; t<observations.length; t++){
	        	for(int o=0; o<observations[t].size(); o++){
	        		Types.ACTIONS direction = fromAngle(observations[t].get(o).position.subtract(avatarPosition).theta());
	        		double distance = observations[t].get(o).position.mag();
	        		if(shortestDistance == -1 || distance < shortestDistance){
	        			shortestDistance = distance;
	        			shortestDirection = direction;
	        		}
	        		numbers.put(direction, numbers.get(direction) + 1);
	        	}
	        }
        }
        
        for(Entry<ACTIONS, Integer> num:numbers.entrySet()){
        	data.values.add(num.getValue() * 1.0);
        }
        data.values.add(shortestDistance);
        data.values.add(getDirection(shortestDirection) * 1.0);
	}
	
	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		ArrayList<Tuple> tuples_ = new ArrayList<Tuple>();		
		double [] value;

		ArrayList<Types.ACTIONS> actions = new ArrayList<Types.ACTIONS>();
		ArrayList<Q> next = new ArrayList<Q>();

		actions = stateObs.getAvailableActions();
		value = new double[actions.size()];
		for (Types.ACTIONS action: actions) 
		{
			Tuple current = new Tuple();
			if(stateObs.getAvailableActions().contains(ACTIONS.ACTION_USE)){
				current.values.add(1.0);
				current.output = action;
				next.add(new Q(stateObs, action, 1.0, ssh.evaluateState(stateObs) + 1.0));
			}
			else
			{
				current.values.add(0.0);
				current.output = action;
				next.add(new Q(stateObs, action, 0.0, ssh.evaluateState(stateObs) + 0.0));
			}
			if(stateObs.getAvailableActions().contains(ACTIONS.ACTION_UP) || 
					stateObs.getAvailableActions().contains(ACTIONS.ACTION_DOWN)){
				current.values.add(1.0);
				current.output = action;
				next.add(new Q(stateObs, action, 1.0, ssh.evaluateState(stateObs) + 1.0));
			}
			else{
				current.values.add(0.0);
				current.output = action;
				next.add(new Q(stateObs, action, 0.0, ssh.evaluateState(stateObs) + 0.0));
			}

			Vector2d avatarPosition = stateObs.getAvatarPosition();

			analyzeData(stateObs.getResourcesPositions(), avatarPosition, current);
			analyzeData(stateObs.getNPCPositions(), avatarPosition, current);
			analyzeData(stateObs.getImmovablePositions(), avatarPosition, current);
			analyzeData(stateObs.getMovablePositions(), avatarPosition, current);
			analyzeData(stateObs.getPortalsPositions(), avatarPosition, current);
			tuples_.add(current);
		}

		for (Tuple tuple : tuples_) 
		{
			tuple.normalize(maxValues);
		}

		for (int i = 0; i < value.length; i++) 
		{
			next.get(i).value = dotProduct(weights, tuples_.get(i).values);
		}
		
		int chooseAction = greedy_epsilon_selection(next, 0.15);
		currentQsa = next.get(chooseAction);
		
		updateWeights(next, weights, chooseAction);
		
		return currentQsa.action;
	}
	
	public int greedy_epsilon_selection(ArrayList<Q> next, double epsilon)
	{
		int selection = 0;
		Random r = new Random();
		if(r.nextDouble() < epsilon)
		{
			selection = r.nextInt(next.size()-1);
		}else{
			int count = 0;
			double comparison = 0.0;
			for (int i = 0; i < next.size(); i++) 
			{
				if(next.get(i).value >= comparison)
				{
					selection = i;
				}
			}
		}
		return selection;
	}
	
	public void updateWeights(ArrayList<Q> next, double [] weights, int epsilon_choice)
	{
		for (int i = 0; i < weights.length; i++) 
		{
			double complement = discountFactor * (next.get(epsilon_choice).scr - currentQsa.value);
			double q_Factor = currentQsa.value + alpha * (next.get(epsilon_choice).scr + complement);
			weights[i] = weights[i] + q_Factor;
		}
	}
	
	private ACTIONS fromAngle(double angle){
    	if(angle >= Math.PI / 4 && angle < 3 * Math.PI / 4) return ACTIONS.ACTION_DOWN;
    	if(angle >= -3 * Math.PI / 4 && angle < -Math.PI / 4) return ACTIONS.ACTION_UP;
    	if(angle >= 3 * Math.PI / 4 || angle < -3 * Math.PI / 4) return ACTIONS.ACTION_LEFT;
    	return ACTIONS.ACTION_RIGHT;
    }
	
	private int getDirection(Types.ACTIONS action){
		if(action == Types.ACTIONS.ACTION_LEFT){
			return 0;
		}
		if(action == Types.ACTIONS.ACTION_RIGHT){
			return 1;
		}
		if(action == Types.ACTIONS.ACTION_UP){
			return 2;
		}
		if(action == Types.ACTIONS.ACTION_DOWN){
			return 3;
		}
		if(action == Types.ACTIONS.ACTION_USE){
			return 4;
		}
		return -1;
	}
	
	private Types.ACTIONS getDirection(int action){
		if(action == 0){
			return Types.ACTIONS.ACTION_LEFT;
		}
		if(action == 1){
			return Types.ACTIONS.ACTION_RIGHT;
		}
		if(action == 2){
			return Types.ACTIONS.ACTION_UP;
		}
		if(action == 3){
			return Types.ACTIONS.ACTION_DOWN;
		}
		if(action == 4){
			return Types.ACTIONS.ACTION_USE;
		}
		return Types.ACTIONS.ACTION_NIL;
	}
	
	public double dotProduct(double [] weights, double [] features)
	{
		double sum = 0.0;

		for (int i = 0; i < features.length; i++) 
		{
			sum = sum + weights[i] * features[i];
		}
		sum = sum + weights[weights.length-1];
		return sum;
	}
	
	public double dotProduct(double [] weights, ArrayList<Double> features)
	{
		double sum = 0.0;

		for (int i = 0; i < features.size(); i++) 
		{
			sum = sum + weights[i] * features.get(i);
		}
		sum = sum + weights[weights.length-1];
		return sum;
	}

	public class Tuple implements Comparable<Tuple>{
		public ArrayList<Double> values;
		public Types.ACTIONS output;
		public double distance;
		
		public Tuple(){
			values = new ArrayList<Double>();
			output = Types.ACTIONS.ACTION_NIL;
		}
		
		public Tuple(String line){
			this();
			String[] parts = line.split(",");
			String line2 = "";
			for (String string : parts) {
				line2 = line2 + string;
			}
			String [] parts2 = line2.split(" ");
			
			for(int i=0; i<parts2.length - 1; i++){
				values.add(Double.parseDouble(parts2[i]));
			}
			output = getDirection(Integer.parseInt(parts2[parts2.length - 1]));
			values.add(Double.parseDouble(parts2[parts2.length - 1]));
		}
		
		public void normalize(double[] maxValues){
			
			for(int i=0; i<values.size(); i++){
				values.set(i, values.get(i) / maxValues[i]);
			}
		}
		
		public void getDistance(Tuple t){
			double distance = 0;
			for(int i=0; i<values.size(); i++){
				distance += Math.pow((this.values.get(i) - t.values.get(i)), 2);
			}
			this.distance = Math.sqrt(distance);
		}

		@Override
		public int compareTo(Tuple t) {
			if(this.distance < t.distance){
				return -1;
			}
			else if(this.distance > t.distance){
				return 1;
			}
			
			return 0;
		}
	}
	
}