package ru.skypro.lessons.springboot.webcw.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.skypro.lessons.springboot.webcw.pojo.Status;

public class LotResponseDTO {
    private Long id;
    private Status status;
    private String title;
    private String description;
    private int startPrice;
    private int bidPrice;
    private int currentPrice;
    private BidResponseDTO lastBid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }

    public int getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(int bidPrice) {
        this.bidPrice = bidPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BidResponseDTO getLastBid() {
        return lastBid;
    }

    public void setLastBid(BidResponseDTO lastBid) {
        this.lastBid = lastBid;
    }
}
