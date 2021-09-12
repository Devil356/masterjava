package ru.javaops.masterjava.xml.util;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

/**
 * Jaxb не подходит когда у нас большой файл из нескольких гб или мб на вход или если файл надо обрабатывать частями,
 * а не полностью. В Таком случае, работаем с StAX (Streaming Api For XML). Он потоковый, то есть последовательный
 * доступ - мы считываем файл до конца, и назад не можем вернуться
 * Также в javadoc ничего не написано про thread-safe, а значит, что она thread-safe по-умолчанию, и значит слово
 * synchronized не нужно.
 */
public class StaxStreamProcessor implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private final XMLStreamReader reader;

    public StaxStreamProcessor(InputStream is) throws XMLStreamException {
        reader = FACTORY.createXMLStreamReader(is);
    }

    public XMLStreamReader getReader() {
        return reader;
    }

    /**
     * Можно подать в @param stopEvent CHARACTER и в @param value нужный текст. Либо START_ELEMENT.
     * См. спецификацию XMLEvent.
     * @return
     * @throws XMLStreamException
     */
    public boolean doUntil(int stopEvent, String value) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == stopEvent) {
                /**
                 * вместо reader.getLocalName() используется getValue по той причине, что можно столкнуться с такой
                 * ситуацией, когда не у всех элементов есть имена. getValue() - более универсальный метод
                 */
                if (value.equals(getValue(event))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getValue(int event) {
        return (event == XMLEvent.CHARACTERS) ? reader.getText() : reader.getLocalName();
    }

    public String getElementValue(String element) throws XMLStreamException {
        return doUntil(XMLEvent.START_ELEMENT, element) ? reader.getElementText() : null;
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                //Empty
            }
        }
    }
}
