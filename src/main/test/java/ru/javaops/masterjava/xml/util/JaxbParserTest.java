package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;
import org.xml.sax.SAXException;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.junit.Assert.*;

class JaxbParserTest {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(
                Schemas.ofClassPath("/userSchema.xsd")
        );
    }

    @Test
    public void testPayload() throws IOException, JAXBException, SAXException {
        Payload payload = JAXB_PARSER
                .unmarshal(Resources.getResource(JaxbParserTest.class, "/payload.xml").openStream());
        String strPayload = JAXB_PARSER.marshal(payload);
        JAXB_PARSER.validate(strPayload);
        System.out.println(strPayload);
    }

    @Test
    public void name() {

    }

}