package ru.skypro.lessons.springboot.webcw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.webcw.dto.BidDTO;
import ru.skypro.lessons.springboot.webcw.dto.BidResponseDTO;
import ru.skypro.lessons.springboot.webcw.dto.LotDTO;
import ru.skypro.lessons.springboot.webcw.dto.LotResponseDTO;
import ru.skypro.lessons.springboot.webcw.exception.LotNotFoundException;
import ru.skypro.lessons.springboot.webcw.exception.NoBiddersException;
import ru.skypro.lessons.springboot.webcw.pojo.Status;
import ru.skypro.lessons.springboot.webcw.service.BidService;
import ru.skypro.lessons.springboot.webcw.service.LotService;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/lot")
@RequiredArgsConstructor
public class LotController {
    private final BidService bidService;
    private final LotService lotService;

    @PostMapping("/create")
    public ResponseEntity<String> createLot(@RequestBody LotDTO lotDTO) {
        return lotService.createLot(lotDTO);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<?> startBidding (@PathVariable("id") Long lotId) {
        try {
            lotService.startBidding(lotId);
            return ResponseEntity.ok("Лот переведен в статус 'Начались торги'");
        } catch (LotNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        }
    }
    @GetMapping("/{id}/first")
    public ResponseEntity<?> getFirstBidForLot(@PathVariable("id") Long lotId) {
        try {
            BidResponseDTO firstBid = bidService.getFirstBidder(lotId);
            return ResponseEntity.ok(firstBid);
        } catch (LotNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        }
        catch (NoBiddersException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("На лот не сделаны ставки");
        }
    }

    @GetMapping("/{id}/frequent")
    public ResponseEntity<?> getMostFrequentBidderForLot(@PathVariable("id") Long lotId) {
        try {
            String mostFrequentBidder = bidService.getMostFrequentBidder(lotId);
            return ResponseEntity.ok(mostFrequentBidder);
        } catch (LotNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        } catch (NoBiddersException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("На лот не сделаны ставки");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLotInfo(@PathVariable("id") Long lotId) {
        try {
            LotResponseDTO lotInfo = lotService.getLotInfo(lotId);
            return ResponseEntity.ok(lotInfo);
        } catch (LotNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        }
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<?> stopBidding (@PathVariable("id") Long lotId) {
        try {
            lotService.stopBidding(lotId);
            return ResponseEntity.ok("Лот переведен в статус 'Торги окончены'");
        } catch (LotNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        }
    }

    @GetMapping
    public ResponseEntity<Page<LotDTO>> getPaginatedLots(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "status", required = false) Status status) {
        Page<LotDTO> lotPage = lotService.getPaginatedLots(page, status);
        return ResponseEntity.ok(lotPage);
    }

    @GetMapping(value = "/export", produces = "application/csv")
    public ResponseEntity<Resource> exportLotsToCSV() {
        try {
            byte[] csvData = lotService.generateCSV();

            ByteArrayResource resource = new ByteArrayResource(csvData);

            return ResponseEntity.ok()
                    .contentLength(csvData.length)
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lots.csv")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
}
    }
