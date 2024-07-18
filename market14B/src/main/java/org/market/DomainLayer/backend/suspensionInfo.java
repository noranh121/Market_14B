package org.market.DomainLayer.backend;

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
            return "suspension started at: " + start.toString() + " - "
                    + "suspension is permanent";
        }
        return "suspension started at: " + start.toString() + " - "
                + "suspension ends at: " + end.toString() + " - "
                + "duration is: " + durationInSeconds;
    }

}
