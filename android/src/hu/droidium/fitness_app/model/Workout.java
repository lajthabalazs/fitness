package hu.droidium.fitness_app.model;

import java.util.List;

public abstract class Workout {
	
	public abstract List<Block> getBlocks();
	
	public final int getMaxRep(){
		int maxRep = 0;
		for(Block block : getBlocks()) {
			for (Exercise exercise : block.getExercises()) {
				maxRep = Math.max(maxRep, exercise.getReps());
			}
		}
		return maxRep;
	}
	
	public final int getNumberOfBlocks(){
		return getBlocks().size();
	}
	
	public final int getNumberOfExercises() {
		int total = 0;
		for(Block block : getBlocks()) {
			total += block.getExercises().size();
		}
		return total;
	}
}