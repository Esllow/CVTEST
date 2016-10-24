package com.esllo.mcdetect;

// Logger Sysout
public class Logger {
	
	// Not used.
	public void debug(String text) {
//		System.out.println(text);
	}

	// Not used.
	public void info(String text) {
//		System.out.println(text);
	} 

	public void warn(String text) {
		System.err.println(text);
	}

	public void error(String text) {
		System.err.println(text); 
	}
}
