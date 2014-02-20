package com.blogspot.nurkiewicz.onion.ns;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class EntityBeanDefinitionParser extends AbstractBeanDefinitionParser {

	private static final String NS = "http://nurkiewicz.blogspot.com/spring/onion/spring-onion.xsd";
	private static final String PKG = "com.blogspot.nurkiewicz.onion.";

	@Override
	protected AbstractBeanDefinition parseInternal(Element entityElement, ParserContext parserContext) {
		final String clazz = entityElement.getAttribute("class");
		final String converters = entityElement.getAttribute("converters");
		register(repositoryBeanDef(clazz), repoBeanName(clazz), parserContext);
		register(serviceBeanDef(clazz, converters), serviceBeanName(clazz), parserContext);
		register(controllerBeanDef(entityElement, clazz), controllerBeanName(clazz), parserContext);
		return null;
	}

	private AbstractBeanDefinition controllerBeanDef(Element entityElement, String clazz) {
		return BeanDefinitionBuilder.
					rootBeanDefinition(controllerBeanName(clazz)).
					addConstructorArgReference(serviceBeanName(clazz)).
					addPropertyValue("errorMapping", extractErrorMappings(entityElement)).
					setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE).
					getBeanDefinition();
	}

	private String controllerBeanName(String clazz) {
		return PKG + clazz + "Controller";
	}

	private ImmutableMap<Integer, String> extractErrorMappings(Element entityElement) {
		final NodeList pageNodes = entityElement.getElementsByTagNameNS(NS, "page");
		final ImmutableMap.Builder<Integer, String> errorMappingBuilder = new ImmutableMap.Builder<>();
		for (int i = 0; i < pageNodes.getLength(); ++i) {
			Element pageElem = (Element) pageNodes.item(i);
			final int response = Integer.parseInt(pageElem.getAttribute("response"));
			final String dest = pageElem.getAttribute("dest") + ".html";
			errorMappingBuilder.put(response, dest);
		}
		return errorMappingBuilder.build();
	}

	private AbstractBeanDefinition serviceBeanDef(String clazz, String converters) {
		final List<BeanMetadataElement> converterRefs = converterRefs(converterNames(converters));
		return BeanDefinitionBuilder.
				rootBeanDefinition(PKG + clazz + "Service").
				addConstructorArgReference(repoBeanName(clazz)).
				addPropertyValue("converters", converterRefs).
				getBeanDefinition();
	}

	private List<BeanMetadataElement> converterRefs(ArrayList<String> converterNames) {
		List<BeanMetadataElement> converterRefs = new ManagedList<>();
		for (String converterName : converterNames) {
			converterRefs.add(new RuntimeBeanReference(converterName + "Converter"));
		}
		return converterRefs;
	}

	private ArrayList<String> converterNames(String convertersStr) {
		final String converters = Strings.nullToEmpty(convertersStr);
		return Lists.newArrayList(
				Splitter.on(',').trimResults().
						split(converters));
	}

	private String serviceBeanName(String clazz) {
		return clazz.toLowerCase() + "Service";
	}

	private String repoBeanName(String clazz) {
		return clazz.toLowerCase() + "Repository";
	}

	private AbstractBeanDefinition repositoryBeanDef(String clazz) {
		return BeanDefinitionBuilder.
					rootBeanDefinition(PKG + clazz + "Repository").
					setInitMethodName("init").
					setDestroyMethodName("destroy").
					addPropertyValue("entityName", clazz).
					addPropertyValue("table", clazz.toUpperCase()).
					addPropertyReference("dataSource", "dataSource").
					addPropertyReference("transactionManager", "transactionManager").
					getBeanDefinition();
	}

	private void register(AbstractBeanDefinition def, String name, ParserContext parserContext) {
		parserContext.getRegistry().registerBeanDefinition(name, def);
	}

}
