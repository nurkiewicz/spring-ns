package com.blogspot.nurkiewicz.onion.ns;

import com.blogspot.nurkiewicz.onion.Converter;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ConverterBeanDefinitionParser extends AbstractBeanDefinitionParser {

	@Override
	protected AbstractBeanDefinition parseInternal(Element converterElement, ParserContext parserContext) {
		final String format = converterElement.getAttribute("format");
		final String lenientStr = converterElement.getAttribute("format");
		final boolean lenient = lenientStr != null? Boolean.valueOf(lenientStr) : true;
		AbstractBeanDefinition beanDef = BeanDefinitionBuilder.
				rootBeanDefinition(Converter.class.getName()).
				addConstructorArgValue(format + ".xml").
				addConstructorArgValue(lenient).
				addConstructorArgReference(format + "Reader").
				getBeanDefinition();
		beanDef.setFactoryBeanName("convertersFactory");
		beanDef.setFactoryMethodName("build");

		return null;
	}
}
