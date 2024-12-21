package sfedu.danil.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class HistoryContent {
    private ObjectId id;
    private String className;
    private LocalDateTime createdDate;
    private String actor = "system";
    private String methodName;
    private Map<String, Object> object;
    private Status status;


    public HistoryContent() {
        this.createdDate = LocalDateTime.now();
    }

}
