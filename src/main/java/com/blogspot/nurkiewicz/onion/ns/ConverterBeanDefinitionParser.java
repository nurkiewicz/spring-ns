package com.blogspot.nurkiewicz.onion.ns;

import com.blogspot.nurkiewicz.onion.Converter;
import com.blogspot.nurkiewicz.onion.ReaderFactoryBean;
import com.blogspot.nurkiewicz.onion.ns.xml.XmlConverter;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ConverterBeanDefinitionParser extends AbstractBeanDefinitionParser {

	protected AbstractBeanDefinition parseInternal(Element converterElement, ParserContext parserContext) {
		XmlConverter converter = JaxbHelper.unmarshal(converterElement);
		final String format = converter.getFormat();
		register(converterBeanDefinition(format, converter.isLenient()), format + "Converter", parserContext);
		register(readerBeanDef(format), format + "Reader", parserContext);
		return null;
	}

	private void register(AbstractBeanDefinition def, String name, ParserContext parserContext) {
		parserContext.getRegistry().registerBeanDefinition(name, def);
	}

	private AbstractBeanDefinition readerBeanDef(String format) {
		return BeanDefinitionBuilder.
				rootBeanDefinition(ReaderFactoryBean.class).
				addPropertyValue("format", format).
				getBeanDefinition();
	}

	private AbstractBeanDefinition converterBeanDefinition(String format, boolean lenient) {
		AbstractBeanDefinition converterBeanDef = BeanDefinitionBuilder.
				rootBeanDefinition(Converter.class.getName()).
				addConstructorArgValue(format + ".xml").
				addConstructorArgValue(lenient).
				addPropertyReference("reader", format + "Reader").
				getBeanDefinition();
		converterBeanDef.setFactoryBeanName("convertersFactory");
		converterBeanDef.setFactoryMethodName("build");
		return converterBeanDef;
	}
}
