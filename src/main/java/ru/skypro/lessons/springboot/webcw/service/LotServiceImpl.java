package ru.skypro.lessons.springboot.webcw.service;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.webcw.dto.BidDTO;
import ru.skypro.lessons.springboot.webcw.dto.BidResponseDTO;
import ru.skypro.lessons.springboot.webcw.dto.LotDTO;
import ru.skypro.lessons.springboot.webcw.dto.LotResponseDTO;
import ru.skypro.lessons.springboot.webcw.exception.LotNotFoundException;
import ru.skypro.lessons.springboot.webcw.pojo.Bid;
import ru.skypro.lessons.springboot.webcw.pojo.Lot;
import ru.skypro.lessons.springboot.webcw.pojo.Status;
import ru.skypro.lessons.springboot.webcw.repository.BidRepository;
import ru.skypro.lessons.springboot.webcw.repository.LotRepository;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LotServiceImpl implements LotService {


    private final BidRepository bidRepository;
    private final LotRepository lotRepository;
    private static final Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);

    @Override
    @Transactional
    public ResponseEntity<String> createLot(LotDTO lotDTO) {
        logger.debug("Was invoked method for create lot: {}", lotDTO);
        // Проверяем поля объекта лота
        if (lotDTO.getTitle() == null || lotDTO.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().body("Title is required"); // Возвращаем статус 400 Bad Request с сообщением об ошибке
        }
        if (lotDTO.getStartPrice() <= 0) {
            return ResponseEntity.badRequest().body("Start price must be greater than 0"); // Возвращаем статус 400 Bad Request с сообщением об ошибке
        }
        if (lotDTO.getBidPrice() < 0) {
            return ResponseEntity.badRequest().body("Bid price cannot be negative"); // Возвращаем статус 400 Bad Request с сообщением об ошибке
        }

        // Создаем новый лот
        Lot lot = new Lot();
        lot.setTitle(lotDTO.getTitle());
        lot.setDescription(lotDTO.getDescription());
        lot.setStatus(Status.CREATED);
        lot.setStartPrice(lotDTO.getStartPrice());
        lot.setBidPrice(lotDTO.getBidPrice());

        // Дополнительная логика сохранения лота в базу данных или другие действия...
        lotRepository.save(lot);
        // Возвращаем статус 201 Created с информацией о созданном лоте
        logger.debug("Lot is :{}", lot);
        return ResponseEntity.status(HttpStatus.CREATED).body("Lot created successfully with ID: " + lot.getId());
    }

    @Override
    public void startBidding(Long lotId) {
        logger.debug("Was invoked method for start bidding: {}", lotId);
        // Проверить, что лот существует
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> {
                    logger.error("Лот не найден");
                    return new LotNotFoundException("Лот не найден");
                });

        // Проверить статус лота
        if (lot.getStatus() == Status.BIDS_STARTED) {
            return; // Лот уже находится в состоянии "начато", ничего не делаем
        }

        // Установить статус "начато" для лота
        lot.setStatus(Status.BIDS_STARTED);
        lotRepository.save(lot);
        logger.debug("Lot is :{}", lot);
    }

    @Override
    public LotResponseDTO getLotInfo(Long lotId) {
        logger.debug("Was invoked method for getting lot info: {}", lotId);
        // Проверить, существует ли лот
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> {
                    logger.error("Лот не найден");
                    return new LotNotFoundException("Лот не найден");
                });

        // Получить последнюю ставку для лота
        Bid lastBid = bidRepository.findFirstByLotIdOrderByTimeStampDesc(lotId)
                .orElse(null);

        // Получить текущую цену лота
        int currentPrice = lot.getCurrentPrice();

        // Создать объект LotResponseDTO
        LotResponseDTO lotResponseDTO = new LotResponseDTO();
        lotResponseDTO.setId(lot.getId());
        lotResponseDTO.setStatus(lot.getStatus());
        lotResponseDTO.setTitle(lot.getTitle());
        lotResponseDTO.setDescription(lot.getDescription());
        lotResponseDTO.setStartPrice(lot.getStartPrice());
        lotResponseDTO.setBidPrice(lot.getBidPrice());
        lotResponseDTO.setCurrentPrice(currentPrice);

        // Установить информацию о последней ставке, если она существует
        if (lastBid != null) {
            BidResponseDTO lastBidResponseDTO = new BidResponseDTO(lastBid.getBidderName(), lastBid.getTimeStamp());
            lotResponseDTO.setLastBid(lastBidResponseDTO);
        }
        logger.debug("Lot is :{}", lotResponseDTO);
        return lotResponseDTO;
    }

    @Override
    public void stopBidding(Long lotId) {
        logger.debug("Was invoked method for stop bidding: {}", lotId);
        // Проверить, что лот существует
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> {
                    logger.error("Лот не найден");
                    return new LotNotFoundException("Лот не найден");
                });

        // Проверить статус лота
        if (lot.getStatus() == Status.BIDS_FINISHED) {
            return; // Лот уже находится в состоянии "торги остановлены", ничего не делаем
        }

        // Установить статус "торги окончены" для лота
        lot.setStatus(Status.BIDS_FINISHED);
        lotRepository.save(lot);
        logger.debug("Lot is :{}", lot);
    }

    @Override
    public Page<LotDTO> getPaginatedLots(int page, Status status) {
        logger.info("Метод getPaginatedLots был вызван");
        // Определите объект Pageable для определения страницы и лимита на количество лотов на странице
        Pageable pageable = PageRequest.of(page, 10);

        // Получите страницу лотов с указанным статусом, без информации о текущей цене и победителе
        Page<Lot> lotPage;
        if (status != null) {
            lotPage = lotRepository.findByStatus(status, pageable);
        } else {
            lotPage = lotRepository.findAll(pageable);
        }

        // Преобразуйте список лотов в список DTO лотов
        List<LotDTO> lotDTOs = lotPage.getContent().stream()
                .map(LotDTO::fromLot)
                .collect(Collectors.toList());

        // Создайте объект Page<LotDTO> с преобразованным списком лотов и информацией о странице
        return new PageImpl<>(lotDTOs, pageable, lotPage.getTotalElements());
    }

    @Override
    public List<LotDTO> getAllLots() {
        List<Lot> lots = lotRepository.findAll();
        List<LotDTO> lotsDTO = new ArrayList<>();

        // Для каждого лота, получаем последнюю ставку
        for (Lot lot : lots) {
            Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "timeStamp"));
            List<Bid> lastBids = bidRepository.findLastBidByLotId(lot.getId(), pageable);

            if (!lastBids.isEmpty()) {
                Bid lastBid = lastBids.get(0);
                LotDTO lotDTO = LotDTO.fromLot(lot);
                BidDTO lastBidDTO = BidDTO.fromBid(lastBid);
                lotDTO.setLastBidder(lastBidDTO.getBidderName());
                lotsDTO.add(lotDTO);
            }
        }
        logger.debug("Lots are :{}", lotsDTO);
        return lotsDTO;
    }

    @Override
    public byte[] generateCSV() {
        List<LotDTO> lots = getAllLots();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT)) {
            // Запись заголовка CSV
            csvPrinter.printRecord("id", "title", "status", "lastBidder", "currentPrice");

            // Запись данных лотов в CSV
            for (LotDTO lot : lots) {
                csvPrinter.printRecord(lot.getId(), lot.getTitle(), lot.getStatus(), lot.getLastBidder(), lot.getCurrentPrice());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}



