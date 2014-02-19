package com.blogspot.nurkiewicz.onion;

import org.springframework.beans.factory.FactoryBean;

public class ReaderFactoryBean implements FactoryBean<Reader> {

	private String format;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public Reader getObject() throws Exception {
		return new Reader();
	}

	@Override
	public Class<?> getObjectType() {
		return Reader.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
