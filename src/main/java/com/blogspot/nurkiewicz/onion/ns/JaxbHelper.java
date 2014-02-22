package com.blogspot.nurkiewicz.onion.ns;

import com.google.common.base.Throwables;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JaxbHelper {

	private static final Unmarshaller unmarshaller = create();

	private static Unmarshaller create() {
		try {
			return JAXBContext.newInstance("com.blogspot.nurkiewicz.onion.ns.xml").createUnmarshaller();
		} catch (JAXBException e) {
			throw Throwables.propagate(e);
		}
	}

	public static <T> T unmarshal(Element elem) {
		try {
			return (T) unmarshaller.unmarshal(elem);
		} catch (JAXBException e) {
			throw Throwables.propagate(e);
		}

	}

}
