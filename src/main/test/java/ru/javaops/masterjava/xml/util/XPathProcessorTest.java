package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class XPathProcessorTest {

    @Test
    public void name() throws Exception {
        try(
                InputStream is = Resources.getResource(JaxbParserTest.class, "/payload.xml")
                        .openStream()
                ) {
            XPathProcessor processor = new XPathProcessor(is);
            XPathExpression expression = XPathProcessor.getExpression("/Payload/Cities/City/text()");
            //см документацию
            NodeList nodeList = processor.evaluate(expression, XPathConstants.NODESET);
            IntStream.range(0, nodeList.getLength()).forEach(
                    i -> System.out.println(nodeList.item(i).getNodeValue())
            );
        }
    }
}