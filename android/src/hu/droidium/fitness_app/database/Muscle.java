package hu.droidium.fitness_app.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Muscle {
	@DatabaseField(id=true)
	private String id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String description;
	
	public Muscle() {
	}
	
	public Muscle(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public boolean refresh(DatabaseManager databaseManager) {
		return refresh(databaseManager, false);
	}

	public boolean refresh(DatabaseManager databaseManager, boolean forced) {
		if (name == null || forced) {
			Muscle other = databaseManager.getMuscle(id);
			this.name = other.name;
			this.description = other.description;
			return true;
		} else {
			return false;
		}
	}

	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return id + " " + name + " " + description;
	}
}
