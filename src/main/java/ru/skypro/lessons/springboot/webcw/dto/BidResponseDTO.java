package ru.skypro.lessons.springboot.webcw.dto;

import java.time.Instant;

public class BidResponseDTO {
    private String bidderName;
    private Instant timeStamp;

    public BidResponseDTO(String bidderName, Instant timeStamp) {
        this.bidderName = bidderName;
        this.timeStamp = timeStamp;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }
}
