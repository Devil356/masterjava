package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;
import org.xml.sax.SAXException;
import ru.javaops.masterjava.xml.schema.CityType;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.io.IOException;

import static org.junit.Assert.*;

public class JaxbParserTest {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(
                Schemas.ofClassPath("payload.xsd")
        );
    }

    @Test
    public void payload() throws IOException, JAXBException, SAXException {
        Payload payload = JAXB_PARSER
                .unmarshal(Resources.getResource(JaxbParserTest.class, "/payload.xml").openStream());
        String strPayload = JAXB_PARSER.marshal(payload);
        JAXB_PARSER.validate(strPayload);
        System.out.println(strPayload);
    }

    @Test
    public void city() throws JAXBException, IOException, SAXException {
        JAXBElement<CityType> cityElement1 = JAXB_PARSER
                .unmarshal(Resources.getResource(JaxbParserTest.class, "/city.xml").openStream());
        CityType cityType = cityElement1.getValue();
        /**
         * если есть только cityType, но нет самого JAXBElement, то необходимо его создать, и только затем
         * маршаллизировать.
         */
        JAXBElement<CityType> cityElement2 =
                new JAXBElement<>(new QName("http://javaops.ru", "City"), CityType.class, cityType);
        String strCity = JAXB_PARSER.marshal(cityElement2);
        JAXB_PARSER.validate(strCity);
        System.out.println(strCity);
    }
}