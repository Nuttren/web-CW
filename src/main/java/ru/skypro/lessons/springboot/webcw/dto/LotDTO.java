package ru.skypro.lessons.springboot.webcw.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import ru.skypro.lessons.springboot.webcw.pojo.Lot;
import ru.skypro.lessons.springboot.webcw.pojo.Status;

import java.time.Instant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(description = "Данные лота")
public class LotDTO {
    @Schema(hidden = true)
    private Long id;

    private String title;

    private String description;
    @Schema(hidden = true)
    private Status status;
    @Schema(hidden = true)
    private Instant creationTime;
    @Schema(hidden = true)
    private Instant startTime;
    @Schema(hidden = true)
    private Instant endTime;

    private int startPrice;

    private int bidPrice;
    @Schema(hidden = true)
    private int currentPrice;

    @Schema(hidden = true)
    private BidDTO lastBid;

    public static LotDTO fromLot(Lot lot) {
        LotDTO lotDTO = new LotDTO();
        lotDTO.setId(lot.getId());
        lotDTO.setTitle(lot.getTitle());
        lotDTO.setDescription(lot.getDescription());
        lotDTO.setStatus(lot.getStatus());
        lotDTO.setCreationTime(lot.getCreationTime());
        lotDTO.setStartTime(lot.getStartTime());
        lotDTO.setEndTime(lot.getEndTime());
        lotDTO.setStartPrice(lot.getStartPrice());
        lotDTO.setBidPrice(lot.getBidPrice());
        lotDTO.setCurrentPrice(lot.getCurrentPrice());
        return lotDTO;
    }

    public Lot toLot() {
        Lot lot = new Lot();
        lot.setTitle(this.title);
        lot.setDescription(this.description);
        lot.setStatus(this.status);
        lot.setStartPrice(this.startPrice);
        lot.setBidPrice(this.bidPrice);
        return lot;
    }

    public LotDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(int bidPrice) {
        this.bidPrice = bidPrice;
    }

// Геттеры и сеттеры для всех полей

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BidDTO getLastBid() {
        return lastBid;
    }

    public void setLastBid(BidDTO lastBid) {
        this.lastBid = lastBid;
    }
    @Schema(hidden = true)
    private String lastBidder;

    // остальные методы класса

    public void setLastBidder(String bidderName) {
        this.lastBidder = bidderName;
    }

    public String getLastBidder() {
        return lastBidder;
    }
}
