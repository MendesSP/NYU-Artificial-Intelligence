package perceptron;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import perceptron.Agent.Tuple;

public class Training 
{
	static double LEARNING_RATE = 0.1;
	double[] weights = new double[33];//the last one is the bias
	double localError;
	ArrayList<Output> outputs = new ArrayList<Output>();
	int iter = 0;
	
	public Training(ArrayList<Tuple> tuples)
	{
		//initialize weights arbitrarily
		Random random = new Random();
		for (int i = 0; i < weights.length; i++) {
			weights[i] = random.nextDouble();
		}

		//initialize features
		double[] features = new double[32];
		for (Tuple tuple : tuples)
		{
			int featureCount = 0;
			for (int i = 0; i < tuple.values.size(); i++) 
			{
				features[featureCount] = tuple.values.get(i);
				featureCount ++;	
			}
			Output output = new Output(features, iter % 2);
			outputs.add(output);
			iter = iter + 1;

		}
	}
	
	public double [] updateWeights()
	{
		//update weights
		int trainingCount = 0;
		while(trainingCount < iter)
		{
			double currentClassification = stepFunction(0.0, weights, outputs.get(trainingCount).features);

			localError = outputs.get(trainingCount).classification - currentClassification;
			double [] tempFeatures = outputs.get(trainingCount).features;
			for (int i = 0; i < weights.length-1; i++) 
			{
				weights[i] = weights[i] + 
						(LEARNING_RATE * localError * tempFeatures[i]);
			}
			weights[weights.length-1] = weights[weights.length-1] + LEARNING_RATE * localError;
			trainingCount++;
		}
		return weights;
	}
	
	public double stepFunction(double threshold, double weights[], double features[])
	{
		double sum = 0.0;
		
		for (int i = 0; i < features.length; i++) 
		{
			sum = sum + weights[i] * features[i];
		}
		sum = sum + weights[weights.length-1];
		if(sum >= threshold)
		{
			return 1.0;
		}else{
			return 0.0;
		}
		
	}
	
	public double magnitude(double [] vector)
	{
		double mag = 0.0;
		for (int i = 0; i < vector.length; i++) 
		{
			mag = mag + (vector[i] * vector[i]);
		}
		mag = Math.sqrt(mag);      
		return mag;
	}
	
	public double [] normalize(double [] vector)
	{
		double mag = magnitude(vector);
		for (int i = 0; i < vector.length; i++) 
		{
			vector[i] = vector[i]/mag;
		}
		return vector;
	}
	
}

class Output
{
	double [] features;
	double classification;
	
	public Output(double [] features, double classification)
	{
		this.features = features;
		this.classification = classification;
	}
	
}

class Weights
{
	double [] weights;
	
	public Weights(double [] weights)
	{
		this.weights = weights; 
	}
	
}