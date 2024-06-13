package DomainLayer.backend;

import java.time.LocalDateTime;

public class suspensionInfo {

    LocalDateTime start;
    LocalDateTime end;
    int durationInSeconds;

    public suspensionInfo(LocalDateTime sDate, int duration) {
        this.start = sDate;
        this.end = sDate.plusSeconds(duration);
        this.durationInSeconds = duration;
    }

    public String toString() {
        if (durationInSeconds == 0) {
            return "suspension startd at: " + start.toString() + '\n'
                    + "supension is permanent";
        }
        return "suspenstion startd at: " + start.toString() + '\n'
                + "suspension ends at: " + end.toString() + '\n'
                + "duration is: " + durationInSeconds;
    }

}
