package com.blogspot.nurkiewicz.onion.ns;

import com.blogspot.nurkiewicz.onion.ns.xml.XmlEntity;
import com.blogspot.nurkiewicz.onion.ns.xml.XmlPage;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.ArrayList;
import java.util.List;

public class EntityBeanDefinitionParser extends AbstractBeanDefinitionParser {

	private static final String PKG = "com.blogspot.nurkiewicz.onion.";

	private final Unmarshaller unmarshaller;

	public EntityBeanDefinitionParser() throws JAXBException {
		unmarshaller = JAXBContext.newInstance("com.blogspot.nurkiewicz.onion.ns.xml").createUnmarshaller();
	}


	@Override
	protected AbstractBeanDefinition parseInternal(Element entityElement, ParserContext parserContext) {
		try {
			final XmlEntity entity = (XmlEntity) unmarshaller.unmarshal(entityElement);
			final String clazz = entity.getClazz();
			register(repositoryBeanDef(clazz), repoBeanName(clazz), parserContext);
			register(serviceBeanDef(entity), serviceBeanName(clazz), parserContext);
			register(controllerBeanDef(entity), controllerBeanName(clazz), parserContext);
			return null;
		} catch (JAXBException e) {
			throw Throwables.propagate(e);
		}
	}

	private AbstractBeanDefinition controllerBeanDef(XmlEntity entity) {
		return BeanDefinitionBuilder.
				rootBeanDefinition(controllerBeanName(entity.getClazz())).
				addConstructorArgReference(serviceBeanName(entity.getClazz())).
				addPropertyValue("errorMapping", extractErrorMappings(entity)).
				setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE).
				getBeanDefinition();
	}

	private String controllerBeanName(String clazz) {
		return PKG + clazz + "Controller";
	}

	private ImmutableMap<Integer, String> extractErrorMappings(XmlEntity entity) {
		final ImmutableMap.Builder<Integer, String> errorMappingBuilder = new ImmutableMap.Builder<>();
		for (XmlPage page : entity.getPage()) {
			errorMappingBuilder.put(page.getResponse(), page.getDest() + ".html");
		}
		return errorMappingBuilder.build();
	}

	private AbstractBeanDefinition serviceBeanDef(XmlEntity entity) {
		final List<BeanMetadataElement> converterRefs = converterRefs(converterNames(entity.getConverters()));
		return BeanDefinitionBuilder.
				rootBeanDefinition(PKG + entity.getClazz() + "Service").
				addConstructorArgReference(repoBeanName(entity.getClazz())).
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
