package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.Constants;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ExerciseType {

	@DatabaseField(id=true)
	private String id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String description;
	@DatabaseField
	private String instructions;
	@DatabaseField
	private String unit;
	@DatabaseField
	private String kUnit;
	@DatabaseField
	private float unitWeight;
	@DatabaseField
	private float unitTime;
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
	private ForeignCollection<ExerciseTypeMuscle> muscles;
	
	public ExerciseType() {}
	
	public boolean refresh(DatabaseManager databaseManager) {
		return refresh(databaseManager, false);
	}

	public ExerciseType(String id, String name, String description, String instructions, String unit, String kUnit, float unitWeight, float unitTime, int stamina, int strength, int speed, int flexibility, int balance) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.instructions = instructions;
		this.unit = unit;
		this.kUnit = kUnit;
		this.unitTime = unitTime;
		this.unitWeight = unitWeight;
		this.stamina = stamina;
		this.strength = strength;
		this.speed = speed;
		this.flexibility = flexibility;
		this.balance = balance;
	}

	public boolean refresh(DatabaseManager databaseManager, boolean forced) {
		if (name == null || forced) {
			ExerciseType other = databaseManager.getExerciseType(id);
			this.name = other.name;
			this.description = other.description;
			this.instructions = other.instructions;
			this.unit = other.unit;
			this.kUnit = other.kUnit;
			this.unitWeight = other.unitWeight;
			this.unitTime = other.unitTime;
			this.stamina = other.stamina;
			this.strength = other.strength;
			this.speed = other.speed;
			this.flexibility = other.flexibility;
			this.balance = other.balance;
			this.muscles = other.muscles;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUnit(String unit){
		this.unit = unit;
	}

	public String getUnit() {
		if ((unit == null) || (unit.equals(""))) {
			return Constants.REPS_DEFAULT_UNIT;
		}
		return unit;
	}
	
	public void setKUnit(String unit){
		this.unit = unit;
	}

	public String getKUnit() {
		if ((kUnit == null) || (kUnit.equals(""))) {
			return null;
		}
		return kUnit;
	}

	public float getUnitWeight() {
		return unitWeight;
	}

	public void setUnitWeight(float unitWeight) {
		this.unitWeight = unitWeight;
	}

	public float getUnitTime() {
		return unitTime;
	}

	public void setUnitTime(float unitTime) {
		this.unitTime = unitTime;
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

	public void setMuscles(ForeignCollection<ExerciseTypeMuscle> muscles) {
		this.muscles = muscles;
	}
	
	public List<Muscle> getMuscles() {
		if (this.muscles == null) {
			return null;
		}
		ArrayList<Muscle> ret = new ArrayList<Muscle>();
		if (muscles != null) {
			for (ExerciseTypeMuscle muscle : muscles) {
				ret.add(muscle.getMuscle());
			}
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return name;
	}
}