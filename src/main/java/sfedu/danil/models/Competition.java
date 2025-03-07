package sfedu.danil.models;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;
import sfedu.danil.LocalDateTimeConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Root
public class Competition {
    @Element
    private String id;

    @Element
    private String name;

    @Element
    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime date;

    public Competition(String name, LocalDateTime date) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.date = date;

    }
    public Competition() {
    }

    @Override
    public String toString() {
        return "Competition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
