package ru.skypro.lessons.springboot.webcw.pojo;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table (name = "bids")
public class Bid {
    @Id
    @Column(name = "bid_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_id")
    private Long lotId;

    @Column(name = "bid_bidder_name")
    private String bidderName;


    @CreationTimestamp
    @Column(name = "bid_timestamp")
    private Instant timeStamp;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id", insertable=false, updatable=false)
    private Lot lot;

    public Bid () {};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
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

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }
}
