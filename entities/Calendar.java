package entities;
import java.time.*;

public class Calendar {

    private LocalDateTime startingDateTime;
    private LocalDateTime endDateTime;
    private Boolean reserved;

    public Calendar(LocalDateTime startingDateTime, LocalDateTime endDateTime, Boolean reserved) {
        this.startingDateTime = startingDateTime;
        this.endDateTime = endDateTime;
        this.reserved = reserved;
    }

    public LocalDateTime getStartingDateTime() {
        return this.startingDateTime;
    }

    public void setStartingDateTime(LocalDateTime startingDateTime) {
        this.startingDateTime = startingDateTime;
    }

    public LocalDateTime getDuration() {
        return this.endDateTime;
    }

    public void setDuration(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }
}
