package hu.droidium.fitness_app.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import hu.droidium.fitness_app.model.ExerciseType;

@DatabaseTable
public class ORMExerciseType implements ExerciseType {

	@DatabaseField(id=true)
	private String id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String unit;
	@DatabaseField
	private int stamina;
	@DatabaseField
	private int strength;
	@DatabaseField
	private int speed;
	@DatabaseField
	private int flexibility;
	@DatabaseField
	private int balance;
	@ForeignCollectionField
	private ForeignCollection<ORMExerciseTypeMuscle> muscles;
	
	public ORMExerciseType(String id, String name, String description, String instructions, String unit, int stamina, int strength, int speed, int flexibility, int balance) {}

	public ORMExerciseType() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setUnit(String unit){
		this.unit = unit;
	}
	@Override
	public String getUnit() {
		return unit;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getFlexibility() {
		return flexibility;
	}

	public void setFlexibility(int flexibility) {
		this.flexibility = flexibility;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public void setMuscles(ForeignCollection<ORMExerciseTypeMuscle> muscles) {
		this.muscles = muscles;
	}
	
	public List<ORMMuscle> getMuscles() {
		ArrayList<ORMMuscle> ret = new ArrayList<ORMMuscle>();
		for (ORMExerciseTypeMuscle muscle:muscles) {
			ret.add(muscle.getMuscle());
		}
		return ret;
	}

	/**
	 * WARNING use only after object has been saved to database and reloaded
	 */
	public void updateMuscles(List<ORMMuscle> muscles,
			Dao<ORMExerciseTypeMuscle, String> exerciseTypeMuscleDao) {
		try {
			exerciseTypeMuscleDao.delete(this.muscles);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.muscles.clear();
		for (ORMMuscle muscle : muscles) {
			ORMExerciseTypeMuscle muscleHelper = new ORMExerciseTypeMuscle(muscle, this);
			try {
				exerciseTypeMuscleDao.createIfNotExists(muscleHelper);
				this.muscles.add(muscleHelper);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}