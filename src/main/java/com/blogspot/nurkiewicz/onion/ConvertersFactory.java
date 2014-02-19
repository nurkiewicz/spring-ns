package com.blogspot.nurkiewicz.onion;

public class ConvertersFactory {
	public Converter build(String file, boolean lenient) {
		return new Converter();
	}
}
