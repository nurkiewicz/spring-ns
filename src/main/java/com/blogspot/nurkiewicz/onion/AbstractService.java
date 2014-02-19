package com.blogspot.nurkiewicz.onion;

import java.util.List;

public class AbstractService {
	protected final AbstractRepository fooRepository;
	private List<Converter> converters;

	public AbstractService(AbstractRepository fooRepository) {
		this.fooRepository = fooRepository;
	}

	public void setConverters(List<Converter> converters) {
		this.converters = converters;
	}
}
