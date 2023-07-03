package ru.skypro.lessons.springboot.webcw.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.webcw.dto.BidDTO;
import ru.skypro.lessons.springboot.webcw.dto.BidResponseDTO;
import ru.skypro.lessons.springboot.webcw.exception.InvalidStateException;
import ru.skypro.lessons.springboot.webcw.exception.LotNotFoundException;
import ru.skypro.lessons.springboot.webcw.exception.NoBiddersException;
import ru.skypro.lessons.springboot.webcw.pojo.Bid;
import ru.skypro.lessons.springboot.webcw.pojo.Lot;
import ru.skypro.lessons.springboot.webcw.pojo.Status;
import ru.skypro.lessons.springboot.webcw.repository.BidRepository;
import ru.skypro.lessons.springboot.webcw.repository.LotRepository;

import javax.accessibility.AccessibleState;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService{

    private final BidRepository bidRepository;
    private final LotRepository lotRepository;

    private static final Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);

    @Override
    public BidDTO createBid(Long lotId, BidDTO bidDTO) {
        logger.debug("Was invoked method for create bid: {}", bidDTO);
        // Проверить, что лот существует
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> {
                    logger.error("Лот не найден");
                    return new LotNotFoundException("Лот не найден");
                });

        // Проверить статус лота
        if (lot.getStatus() != Status.BIDS_STARTED) {
            throw new InvalidStateException("Лот в неверном статусе");
        }

        // Создать объект ставки
        Bid bid = new Bid();
        bid.setLotId(lotId);
        bid.setBidderName(bidDTO.getBidderName());
        bid.setTimeStamp(Instant.now());

        // Сохранить ставку
        Bid savedBid = bidRepository.save(bid);
        logger.debug("Bid is :{}", bid);
        // Преобразовать и вернуть DTO ставки
        return BidDTO.fromBid(savedBid);
    }

    @Override
    public BidResponseDTO getFirstBidder(Long lotId) {
        logger.debug("Was invoked method for getting lot: {}", lotId);
        // Проверить, что лот существует
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> {
                    logger.error("Лот не найден");
                    return new LotNotFoundException("Лот не найден");
                });

        // Получить первую ставку на лот
        Bid firstBid = bidRepository.findFirstByLotIdOrderByTimeStampAsc(lotId)
                .orElseThrow(() -> {
                    logger.error("На лот еще не сделаны ставки");
                    return new NoBiddersException("На лот еще не сделаны ставки");
                });
        BidResponseDTO responseDTO = new BidResponseDTO(firstBid.getBidderName(), firstBid.getTimeStamp());
        logger.debug("First bid is :{}", responseDTO);
        // Преобразовать и вернуть DTO ставки
        return responseDTO;
    }

    @Override
    public String getMostFrequentBidder(Long lotId) {
        // Проверить, что лот существует
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> {
                    logger.error("Лот не найден");
                    return new LotNotFoundException("Лот не найден");
                });

        // Получить наиболее частого ставщика для данного лота
        List<String> bidders = bidRepository.findMostFrequentBiddersByLotId(lotId);
        String mostFrequentBidder = bidders.get(0);
        if (mostFrequentBidder == null) {
            throw new NoBiddersException("На лот не сделаны ставки");
        }

        return mostFrequentBidder;
    }
}
