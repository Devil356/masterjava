package ru.javaops.masterjava.xml.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * XPath - язык для извлечения данных из XML
 * XPath - не thread-safe. Поэтому используется synchronized.
 */
public class XPathProcessor {
    private static final DocumentBuilderFactory DOCUMENT_FACTORY = DocumentBuilderFactory.newInstance();
    public static final DocumentBuilder DOCUMENT_BUILDER;

    public static final XPathFactory X_PATH_FACTORY = XPathFactory.newInstance();
    public static final XPath X_PATH = X_PATH_FACTORY.newXPath();

    static {
        //Это всегда обязательно выставлять. Почему не сделано по-умолчанию - не ясно.
        DOCUMENT_FACTORY.setNamespaceAware(true);
        try {
            DOCUMENT_BUILDER = DOCUMENT_FACTORY.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private final Document doc;

    public static synchronized XPathExpression getExpression(String exp){
        try {
            return X_PATH.compile(exp);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public XPathProcessor(InputStream is) {
        try {
            doc = DOCUMENT_BUILDER.parse(is);
        } catch (SAXException | IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> T evaluate(XPathExpression exp, QName type) {
        try {
            return (T) exp.evaluate(doc, type);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
