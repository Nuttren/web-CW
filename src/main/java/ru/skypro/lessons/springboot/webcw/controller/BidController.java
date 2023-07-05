package ru.skypro.lessons.springboot.webcw.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.webcw.dto.BidDTO;
import ru.skypro.lessons.springboot.webcw.exception.InvalidStateException;
import ru.skypro.lessons.springboot.webcw.exception.LotNotFoundException;
import ru.skypro.lessons.springboot.webcw.service.BidService;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/bid")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @PostMapping("/{id}/bid")
    public ResponseEntity<?> createBid(@PathVariable("id") Long lotId, @RequestBody BidDTO bidDTO) {
        try {
            bidService.createBid(lotId, bidDTO);
            return ResponseEntity.ok("Ставка создана"); // Return the successfully created bid
        } catch (InvalidStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Лот в неверном статусе"); // Return status 400 with error description
        } catch (LotNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден"); // Return status 404 with error description
        }
    }

}
