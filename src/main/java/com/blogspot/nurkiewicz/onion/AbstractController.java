package com.blogspot.nurkiewicz.onion;

import java.util.Map;

public class AbstractController {
	protected final AbstractService fooService;
	private Map<Integer, String> errorMapping;

	public AbstractController(AbstractService fooService) {
		this.fooService = fooService;
	}

	public void setErrorMapping(Map<Integer, String> errorMapping) {
		this.errorMapping = errorMapping;
	}
}
