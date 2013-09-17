package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Workout{
	
	@DatabaseField(id=true)
	private String id;
	@DatabaseField(foreign=true)
	private Program program;
	@DatabaseField
	private String name;
	@DatabaseField
	private int day; // The day of the program the workout is scheduled, starting from the beginning of the program
	@DatabaseField
	private String description;
	@ForeignCollectionField(orderColumnName="order")
	private ForeignCollection<Block> blocks;

	public Workout(){
	}
	
	public Workout(String id, Program program, int day, String name, String description) {
		this.id = id; 
		this.program = program;
		this.day = day;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBlocks(ForeignCollection<Block> blocks) {
		this.blocks = blocks;
	}

	public List<Block> getBlocks() {
		ArrayList<Block> ret = new ArrayList<Block>();
		for (Block block : blocks) {
			ret.add(block);
		}
		return ret;
	}
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