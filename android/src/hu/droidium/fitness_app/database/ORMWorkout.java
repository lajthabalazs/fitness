package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.model.Block;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ORMWorkout extends hu.droidium.fitness_app.model.Workout{
	
	@DatabaseField(id=true)
	private String id;
	@DatabaseField(foreign=true)
	private ORMProgram program;
	@DatabaseField
	private String name;
	@DatabaseField
	private int day; // The day of the program the workout is scheduled, starting from the beginning of the program
	@DatabaseField
	private String description;
	@ForeignCollectionField(orderColumnName="order")
	private ForeignCollection<ORMBlock> blocks;

	public ORMWorkout(){
	}
	
	public ORMWorkout(String id, int day, String name, String description) {
		this.id = id; 
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
	
	public ORMProgram getProgram() {
		return program;
	}

	public void setProgram(ORMProgram program) {
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
	
	@Override
	public String getDescription() {
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBlocks(ForeignCollection<ORMBlock> blocks) {
		this.blocks = blocks;
	}

	@Override
	public List<Block> getBlocks() {
		ArrayList<Block> ret = new ArrayList<Block>();
		for (ORMBlock block : blocks) {
			ret.add(block);
		}
		return ret;
	}

	/**
	 * WARNING use only after object has been saved to database and reloaded
	 * @param blocks
	 */
	public void updateBlocks(List<ORMBlock> blocks) {
		this.blocks.clear();
		this.blocks.addAll(blocks);
	}
}