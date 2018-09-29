package uprizing.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class SettingUtils {

	private static final char SEPARATOR = ':';

	public static void saveToFile(File file, BaseSettings settings) {
		try {
			final PrintWriter writer = new PrintWriter(new FileWriter(file));
			int cursor = 0;

			while (cursor != settings.count()) {
				final Setting setting = settings.get(cursor++);
				writer.println(setting.getSexyName() + SEPARATOR + setting.bar());
			}

			writer.close();
		} catch (Exception exception) {
			System.out.println("Failed to save options.");
			exception.printStackTrace();
		}
	}

	public static void loadFromFile(File file, BaseSettings settings) {
		try {
			if (!file.exists()) return;

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			while ((line = reader.readLine()) != null) {
				try {
					final String[] keyAndValue = getKeyAndValue(line);
					int cursor = 0;

					while (cursor != settings.count()) {
						final Setting setting = settings.get(cursor++);
						if (setting.getSexyName().equals(keyAndValue[0])) {
							setting.foo(keyAndValue[1]);
						}
					}
				} catch (Exception exception) {
					System.out.println("Skipping bad setting: " + line);
					exception.printStackTrace();
				}
			}

			reader.close();
		} catch (Exception exception) {
			System.out.println("Failed to load settings.");
			exception.printStackTrace();
		}
	}

	private static String[] getKeyAndValue(String line) {
		int cursor = 0;

		while (line.charAt(cursor) != SEPARATOR) {
			cursor++;
		}

		final String key = line.substring(0, cursor);
		final String value = line.substring(cursor + 1, line.length());

		return new String[] { key, value };
	}
}