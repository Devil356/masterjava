package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import java.io.IOException;

import static org.junit.Assert.*;

public class StaxStreamProcessorTest {

    /**
     * В блоке try можно размещать только autoCloseable ресурсы
     */
    @Test
    public void readCities() throws Exception {
        try(StaxStreamProcessor processor =
                    new StaxStreamProcessor(Resources.getResource(StaxStreamProcessor.class, "/payload.xml")
                            .openStream())) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    //Local Name - это имя элемента
                    if ("City".equals(reader.getLocalName())) {
                        System.out.println(reader.getElementText());
                    }
                }
            }
        }
    }

    @Test
    public void readCities2() throws Exception {
        try(StaxStreamProcessor processor =
                    new StaxStreamProcessor(Resources.getResource(StaxStreamProcessor.class, "/payload.xml")
                            .openStream())) {
            String city;
            while ((city = processor.getElementValue("City")) != null) {
                System.out.println(city);
            }
        }
    }
}