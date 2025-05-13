package sfedu.danil.models.mappedSingletable;

import jakarta.persistence.*;
import lombok.*;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;
import sfedu.danil.LocalDateTimeConverter;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "CompetitionSingleTable")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "competition_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("null")
@Root
public class Competition {

    @Id
    private String id;


    private String name;


    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime date;

    @ElementCollection
    @CollectionTable(
            name = "competition_images",
            joinColumns = @JoinColumn(name = "competition_id")
    )
    @OrderColumn(name = "images_order")
    @Column(name = "filename")
    private List<String> imageFilenames = new ArrayList<>();

    public Competition(String name, LocalDateTime date) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.date = date;

    }

    public Competition() {
    }
}