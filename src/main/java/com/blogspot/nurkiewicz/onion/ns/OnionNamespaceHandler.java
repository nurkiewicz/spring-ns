package com.blogspot.nurkiewicz.onion.ns;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class OnionNamespaceHandler extends NamespaceHandlerSupport {
	public void init() {
		registerBeanDefinitionParser("entity", new EntityBeanDefinitionParser());
		registerBeanDefinitionParser("converter", new ConverterBeanDefinitionParser());
	}
}
