package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class XsltProcessorTest {

    @Test
    public void transform() throws IOException, TransformerException {
        try(InputStream xslInputStream = Resources.getResource(JaxbParserTest.class, "/cities.xsl").openStream();
            //Файл, на который будем налагать нашу трансформацию
            InputStream xmlInputStream = Resources.getResource(JaxbParserTest.class, "/payload.xml").openStream()
        ) {
            XsltProcessor processor = new XsltProcessor(xslInputStream);
            System.out.println(processor.transform(xmlInputStream));
        }
    }
}