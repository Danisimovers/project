package sfedu.danil.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import static sfedu.danil.Constants.*;

@Entity
@Table(name = TEST_ENT)
@Getter
@Setter
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = DATE_CREAT, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Column(nullable = false)
    private Boolean check;

    @Embedded
    private Metadata metadata;

    public TestEntity() {
    }

    public TestEntity(String name, String description, Boolean check) {
        this.name = name;
        this.description = description;
        this.check = check;
    }

    @PrePersist
    protected void onCreate() {
        this.dateCreated = new Date();
        this.metadata = Objects.requireNonNullElse(this.metadata, new Metadata(DEFAULT_CREATED_BY));
    }

    @PreUpdate
    protected void onUpdate() {
        Optional.ofNullable(metadata).ifPresent(Metadata::updateTimestamp);
    }
}
