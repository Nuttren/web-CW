package ru.skypro.lessons.springboot.webcw.service;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.lessons.springboot.webcw.dto.LotDTO;
import ru.skypro.lessons.springboot.webcw.dto.LotResponseDTO;
import ru.skypro.lessons.springboot.webcw.pojo.Status;

import java.util.List;

public interface LotService {

    ResponseEntity<String> createLot (LotDTO lotDTO);

    void startBidding(Long lotId);

    LotResponseDTO getLotInfo(Long lotId);

    void stopBidding(Long lotId);

    Page<LotDTO> getPaginatedLots(int page, Status status);

    List<LotDTO> getAllLots();

    byte[] generateCSV();

}
