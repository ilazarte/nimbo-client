package com.blm.nimboclient;

import java.io.File;

public interface FileWalkAction {
	
	/**
	 * Handle a file found on the file system. Can be directory or file.
	 * @param file
	 */
	public void handle(File file);
}
