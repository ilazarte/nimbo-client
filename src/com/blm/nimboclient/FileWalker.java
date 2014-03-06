package com.blm.nimboclient;

import java.io.File;

public class FileWalker {
	
	private static final String PERIOD = ".";
	
	/**
	 * Simple walk method, not taking into account potential infinite loops via symlinking!
	 * Skips anything with a period prefix signifying internal.
	 * Does not pass directories into action.
	 * 
	 * @param dir
	 * @param files
	 */
	public void walk(File dir, FileWalkAction action) {

		if (dir.getName().startsWith(PERIOD)) {
			return;
		}
		
		File listFile[] = dir.listFiles();
		
		if (listFile == null) {
			return;
		}
		
		for (int i = 0; i < listFile.length; i++) {
			
			File file = listFile[i];
			
			if (file.isDirectory()) {
				walk(file, action);
				
			} else if (file.getName().startsWith(PERIOD)) {
				continue;
				
			} else {
				action.handle(file);
			}
		}
	}
}
