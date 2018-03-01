package org.obj2openjl.v3.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LineReader {
	
	private BufferedReader bufferedReader;
	public int readCount = 0;
	
	public LineReader(InputStream inputStream) {
		this.bufferedReader = this.getBufferedReaderFrom(inputStream);
	}
	
	public String readLine() {
		String line = null;
		try {
			line = this.bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.readCount++;
		return line;
	}
	
	private BufferedReader getBufferedReaderFrom(InputStream inputStream) {
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		return bufferedReader;
	}

}
