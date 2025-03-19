package sfedu.danil;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter implements org.simpleframework.xml.convert.Converter<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public LocalDateTime read(InputNode node) throws Exception {
        String date = node.getValue();
        return LocalDateTime.parse(date, formatter);
    }

    @Override
    public void write(OutputNode node, LocalDateTime value) throws Exception {
        node.setValue(value.format(formatter));
    }
}