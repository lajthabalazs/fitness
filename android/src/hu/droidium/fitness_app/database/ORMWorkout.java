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
	private long id;
	
	@DatabaseField
	private String name;
	
	@ForeignCollectionField
	private ForeignCollection<ORMBlock> blocks;

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setBlocks(List<ORMBlock> blocks) {
	}

	@Override
	public List<Block> getBlocks() {
		ArrayList<Block> ret = new ArrayList<Block>();
		for (ORMBlock block : blocks) {
			ret.add(block);
		}
		return ret;
	}
}
