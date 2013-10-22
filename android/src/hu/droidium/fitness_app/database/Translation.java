package hu.droidium.fitness_app.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Translation {
	private static final String SEPARATOR = "_";
	@DatabaseField(id=true)
	private String key;
	private String value;
	
	public Translation(String string, String languageCode, String value) {
		this.key = languageCode + SEPARATOR + string;
		this.value = value;
	}
	
	public Translation() {}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public static String getKey(String string, String languageCode) {
		return languageCode + SEPARATOR + string;
	}
	
	@Override
	public String toString() {
		return key + ": " + value; 
	}
}
