package com.libopenmw.openmw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Writer {

	public static void write(String data, String path, String value)
			throws IOException {
		FileInputStream file = new FileInputStream(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(file));
		String line = reader.readLine();
		StringBuilder builder = new StringBuilder();
		while (line != null) {
			if (line.contains(value))
				builder.append(value + "=" + data);
			else
				builder.append(line);
			builder.append("\n");
			line = reader.readLine();
		}
		reader.close();
		FileWriter writer = new FileWriter(path);
		writer.write(builder.toString());
		writer.flush();
		writer.close();

	}
}
