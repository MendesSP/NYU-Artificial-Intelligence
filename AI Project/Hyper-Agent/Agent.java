package controllers.AlgDecision_V2;

import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import core.player.AbstractPlayer;

import controllers.getFeatures.Agent.GameFeatures;
import controllers.sampleMCTS.SingleMCTSPlayer;
import controllers.Heuristics.SimpleStateHeuristic;
import core.game.Event;
import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import ontology.Types.WINNER;
import tools.ElapsedCpuTimer;
import tools.Vector2d;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.core.FastVector;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;

@SuppressWarnings({ "unused", "deprecation" })
public class Agent extends AbstractPlayer {

	// Constructor. It must return in 1 second maximum.

	double decision = 0;
	boolean write_decision = false;
	Instances data;
	Instances data_norm;
	GameFeatures gf1 = null;
	GameFeatures gf2 = null;
	BufferedReader datafile;
	String modelName = "J48";//"AlgDecision";
	Classifier models;
	String filename = "/Users/andremendes/OneDrive/GVGAI-Weka/Arff Files/game.arff";
	AbstractPlayer agent = null;
	long now;
	long after;
	Types.ACTIONS action = null;
	int start = 2;
	Filter filter_norm;

	public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		String modelfile = "/Users/andremendes/OneDrive/GVGAI-Weka/Models/" + modelName + ".model";
		try {
			models = (Classifier) weka.core.SerializationHelper.read(modelfile);
			filter_norm = (Filter) weka.core.SerializationHelper
					.read("/Users/andremendes/OneDrive/GVGAI-Weka/Filters/Norm_filter.filter");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Act function. Called every game step, it must return an action in 40 ms
	// maximum.
	public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		// If it is still capturing the features
		if (stateObs.getGameTick() < start) {
			if (stateObs.getGameTick() == 0) {
				gf1 = new GameFeatures(null);
				gf1.getValues(stateObs);
			}
			// apply ACTION_NIL to guarantee that features are the same as in
			// training data
			while (elapsedTimer.elapsedMillis() < 40)
				start = 2;
			// get the features are the desired point
		} else if (stateObs.getGameTick() == start) {
			// now = System.currentTimeMillis();
			gf2 = new GameFeatures(null);
			gf2.getValues(stateObs);
			// create a file to be used by weka
			WriteARFF(gf2, null, null, null, filename);
			// read the data from arff file
			try {
				datafile = readDataFile(filename);
				data = new Instances(datafile);
				data.setClassIndex(data.numAttributes() - 1);
				// apply some filter as in the training
				data_norm = Filter.useFilter(data, filter_norm);
				// use the models and the data to get the predictions
				decision = models.classifyInstance(data_norm.instance(0));
				agent = SelectAgent(decision, agent, stateObs);
				WriteDecision("/Users/andremendes/OneDrive/GVGAI-Weka/decision.txt",decision+1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else
			action = agent.act(stateObs, elapsedTimer);

		return action;

	}

	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;

		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}

		return inputReader;
	}

	public static Evaluation classify(Classifier model, Instances trainingSet, Instances testingSet) throws Exception {
		Evaluation evaluation = new Evaluation(trainingSet);

		model.buildClassifier(trainingSet);
		evaluation.evaluateModel(model, testingSet);

		return evaluation;
	}

	public static double calculateAccuracy(@SuppressWarnings({ "rawtypes" }) FastVector predictions) {
		double correct = 0;

		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}

		return 100 * correct / predictions.size();
	}

	public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];

		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);
		}

		return split;
	}

	public static class GameFeatures {
		private static final long serialVersionUID = 1L;
		public double GameScore;
		public int GameTick;
		public int isGameOver;
		public double Area;
		public int CanShoot;
		public int MoveVertically;
		public int nNPC;
		public int gameHasResources;
		public int avatarHasResources;
		public int nTypeImmovableSprites;
		public int nTypeMovableSprites;
		public int hasPortals;
		public int nTypePortals;
		public int nTypeNPC;
		public int nTypeResourcesGame;
		public int nTypeResourcesAvatar;
		public int blockSize;
		public int avatarType;

		public GameFeatures(Object object) {
			// TODO Auto-generated constructor stub
			object = null;
		}

		public void getValues(StateObservation stateObs) {

			this.GameTick = stateObs.getGameTick();
			this.Area = stateObs.getWorldDimension().height * stateObs.getWorldDimension().width;
			this.avatarType = stateObs.getAvatarType();
			this.blockSize = stateObs.getBlockSize();

			if (stateObs.isGameOver())
				this.isGameOver = 1;
			else
				this.isGameOver = 0;

			if (stateObs.getImmovablePositions() != null)
				this.nTypeImmovableSprites = stateObs.getImmovablePositions().length;
			else
				this.nTypeImmovableSprites = 0;

			if (stateObs.getMovablePositions() != null)
				this.nTypeMovableSprites = stateObs.getMovablePositions().length;
			else
				this.nTypeMovableSprites = 0;

			if (stateObs.getAvailableActions().toString().contains("ACTION_USE"))
				this.CanShoot = 1;
			else
				this.CanShoot = 0;

			if (stateObs.getAvailableActions().toString().contains("ACTION_UP"))
				this.MoveVertically = 1;
			else
				this.MoveVertically = 0;

			if (stateObs.getResourcesPositions() != null) {
				this.nTypeResourcesGame = stateObs.getResourcesPositions().length;
				this.gameHasResources = 1;
			} else {
				this.nTypeResourcesGame = 0;
				this.gameHasResources = 0;
			}

			if (stateObs.getAvatarResources() != null) {
				this.nTypeResourcesAvatar = stateObs.getAvatarResources().size();
				this.avatarHasResources = 1;
			} else {
				this.nTypeResourcesAvatar = 0;
				this.avatarHasResources = 0;
			}

			if (stateObs.getPortalsPositions() != null) {
				this.hasPortals = 1;
				this.nTypePortals = stateObs.getPortalsPositions().length;
			} else {
				this.hasPortals = 0;
				this.nTypePortals = 0;
			}

			if (stateObs.getNPCPositions() != null)
				this.nTypeNPC = stateObs.getNPCPositions().length;
			else
				this.nTypeNPC = 0;
		}

	}

	public void WriteARFF(GameFeatures gf1, GameFeatures gf2, GameFeatures gf3, GameFeatures gf4, String filename) {
		BufferedWriter writer = null;
		try {
			// create a temporary file
			// String timeLog = new
			// SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			File logFile = new File(filename);
			writer = new BufferedWriter(new FileWriter(logFile));
			String name = "game";
			// writer.write("Game");

			writer.write("@relation " + name + '\n' + '\n');
			writer.write("@attribute GameScore real" + '\n' + "@attribute GameTick real" + '\n'
					+ "@attribute isGameOver real" + '\n' + "@attribute Area real" + '\n' + "@attribute CanShoot real"
					+ '\n' + "@attribute MoveVertically real" + '\n' + "@attribute nNPC real" + '\n'
					+ "@attribute gameHasResources real" + '\n' + "@attribute avatarHasResources real" + '\n'
					+ "@attribute nTypeImmovableSprites real" + '\n' + "@attribute nTypeMovableSprites real" + '\n'
					+ "@attribute hasPortals real" + '\n' + "@attribute nTypePortals real" + '\n'
					+ "@attribute nTypeNPC real" + '\n' + "@attribute nTypeResourcesGame real" + '\n'
					+ "@attribute nTypeResourcesAvatar real" + '\n' + "@attribute blockSize real" + '\n'
					+ "@attribute class {0,1}" + '\n');

			writer.write('\n' + "@data" + '\n');

			if (gf1 != null)
				writer.write(gf1.GameScore + "," + gf1.GameTick + "," + gf1.isGameOver + "," + gf1.Area + ","
						+ gf1.CanShoot + "," + gf1.MoveVertically + "," + gf1.nNPC + "," + gf1.gameHasResources + ","
						+ gf1.avatarHasResources + "," + gf1.nTypeImmovableSprites + "," + gf1.nTypeMovableSprites + ","
						+ gf1.hasPortals + "," + gf1.nTypePortals + "," + gf1.nTypeNPC + "," + gf1.nTypeResourcesGame
						+ "," + gf1.nTypeResourcesAvatar + "," + gf1.blockSize + "," + "0");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	public double[] UseModel(Instances data, Classifier model) throws Exception {

		double aux[] = model.distributionForInstance(data.instance(0));
		double[] per = new double[3];
		for (int i = 0; i < aux.length; i++)
			per[i] = aux[i];
		per[2] = model.classifyInstance(data.instance(0));

		return per;

	}

	public void WriteDecision(String filename, double decision) {
		BufferedWriter writer = null;
		try {
			File logFile = new File(filename);
			writer = new BufferedWriter(new FileWriter(logFile, true));
			writer.write(String.valueOf(decision) + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	public AbstractPlayer SelectAgent(double decision, AbstractPlayer agent, StateObservation stateObs) {
		ElapsedCpuTimer new_time = new ElapsedCpuTimer();
		decision = decision + 1;
		if (decision == 1) {
			agent = new controllers.sampleMCTS.Agent(stateObs, new_time);
		} else if (decision == 2) {
			agent = new controllers.sampleOLMCTS.Agent(stateObs, new_time);
		} else if (decision == 3) {
			agent = new controllers.sampleonesteplookahead.Agent(stateObs, new_time);
		} else if (decision == 4) {
			agent = new YOLOBOT.Agent(stateObs, new_time);
		} else if (decision == 5) {
			agent = new controllers.adrienctx.Agent(stateObs, new_time);
		} else if (decision == 6) {
			agent = new controllers.JinJerry.Agent(stateObs, new_time);
		} else if (decision == 7) {
			agent = new controllers.sampleGA.Agent(stateObs, new_time);
		}
		return agent;
	}
}
