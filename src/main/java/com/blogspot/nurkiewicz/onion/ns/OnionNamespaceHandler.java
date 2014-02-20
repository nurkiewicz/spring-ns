package com.blogspot.nurkiewicz.onion.ns;

import com.google.common.base.Throwables;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import javax.xml.bind.JAXBException;

public class OnionNamespaceHandler extends NamespaceHandlerSupport {
	public void init() {
		try {
			registerBeanDefinitionParser("entity", new EntityBeanDefinitionParser());
			registerBeanDefinitionParser("converter", new ConverterBeanDefinitionParser());
		} catch (JAXBException e) {
			throw Throwables.propagate(e);
		}
	}
}
