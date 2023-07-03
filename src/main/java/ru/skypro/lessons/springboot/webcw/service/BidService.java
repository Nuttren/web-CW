package ru.skypro.lessons.springboot.webcw.service;

import ru.skypro.lessons.springboot.webcw.dto.BidDTO;
import ru.skypro.lessons.springboot.webcw.dto.BidResponseDTO;

public interface BidService {

    BidDTO createBid(Long lotId, BidDTO bidDTO);

    BidResponseDTO getFirstBidder(Long lotId);

    String getMostFrequentBidder(Long lotId);
}
