package ru.skypro.lessons.springboot.webcw.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.skypro.lessons.springboot.webcw.pojo.Bid;

import java.time.Instant;

public class BidDTO {
    @Schema(hidden = true)
    private Long id;

    @Schema(hidden = true)
    private Long lotId;
    private String bidderName;

    @Schema(hidden = true)
    private Instant timeStamp;

    public static BidDTO fromBid(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setId(bid.getId());
        bidDTO.setLotId(bid.getLotId());
        bidDTO.setBidderName(bid.getBidderName());
        bidDTO.setTimeStamp(bid.getTimeStamp());
        return bidDTO;
    }

    public Bid toBid() {
        Bid bid = new Bid();
        bid.setId(this.id);
        bid.setLotId(this.lotId);
        bid.setBidderName(this.bidderName);
        bid.setTimeStamp(this.timeStamp);
        return bid;
    }

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
}
