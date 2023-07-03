package ru.skypro.lessons.springboot.webcw.pojo;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import ru.skypro.lessons.springboot.webcw.dto.BidDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table (name = "lots")
public class Lot {
    @Id
    @Column(name = "lot_id")

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_title")
    private String title;

    @Column(name = "lot_description")
    private String description;
    @Column(name = "lot_status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    @Column (name = "lot_created_at", updatable = false)
    private Instant creationTime;

    @UpdateTimestamp
    @Column (name = "lot_start_time")
    private Instant startTime;

    @UpdateTimestamp
    @Column (name = "lot_end_time")
    private Instant endTime;

    @Column (name = "lot_start_price")
    private int startPrice;

    @Column (name = "bid_price")
    private int bidPrice;

    @Formula("(SELECT COUNT(*) FROM bids WHERE bids.lot_id = lot_id) * bid_price + lot_start_price")
    private int currentPrice;



    public Lot () {};

    public int getBidPrice() {
        return bidPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBidPrice(int bidPrice) {
        this.bidPrice = bidPrice;
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

}
