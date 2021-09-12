package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.StringReader;
import java.net.URL;

/**
 * Многие методы помечены synchronized по той причине, что внутри метода используются не thread-safe библиотеки
 * Не thread-safe означает, что эти библиотеки имеют внутри себя некоторые состояния, и нельзя допускать, чтобы несколько
 * потоков врывались туда. Поэтому или synchronized, или ThreadLocal использовать или еще какие-то вещи
 */
public class Schemas {
    private static final SchemaFactory SCHEMA_FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    public static synchronized Schema ofString(String xsd){
        try {
            return SCHEMA_FACTORY.newSchema(new StreamSource(new StringReader(xsd)));
        } catch (SAXException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static synchronized Schema ofClassPath(String resource) {
        return ofURL(Resources.getResource(resource));
    }

    private static synchronized Schema ofURL(URL url) {
        try {
            return SCHEMA_FACTORY.newSchema(url);
        } catch (SAXException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static synchronized Schema ofFile(File file) {
        try {
            return SCHEMA_FACTORY.newSchema(file);
        } catch (SAXException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
