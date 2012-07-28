/* 
 * Copyright 2011, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.messagereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class that handles message files
 * @author Paulo Henrique Florian
 *
 */
public class MessageReader {
	private File file;
	private List<Message> messages = new ArrayList<Message> ();
	
	public MessageReader (String filename) {
		file = new File(filename);
	}
	
	public MessageReader (File file) {
		this.file = file;
	}
	
	/**
	 * Parses message file
	 * @return List of messages read from the file
	 */
	public List<Message> readMessages () {
		try {
			Scanner sc = new Scanner (file);
			while (sc.hasNext("([0-9]*){3}")) {
				Message m = new Message ();
				m.setCreationTime(sc.nextInt());
				m.setOrigin(sc.next());
				m.setDestination(sc.next());
				messages.add(m);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messages;
	}

}
