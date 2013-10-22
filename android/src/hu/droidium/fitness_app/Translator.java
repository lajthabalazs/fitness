package hu.droidium.fitness_app;

import hu.droidium.fitness_app.database.DatabaseManager;

import java.util.Locale;

/**
 * Convenience class for translating strings from database
 * @author Balazs Lajtha
 *
 */
public class Translator {
	
	
	private static DatabaseManager databaseManager;

	public static void init(DatabaseManager databaseManager) {
		if (Translator.databaseManager == null) {
			Translator.databaseManager = databaseManager;
		}
	}
	
	public static String getTranslation(String string) {
		String languageCode = Locale.getDefault().getDisplayLanguage();
		if (databaseManager == null) {
			return string;
		} else {
			return databaseManager.getTranslation(string, languageCode);
		}
	}
}