package ru.skypro.lessons.springboot.webcw.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.lessons.springboot.webcw.pojo.Lot;
import ru.skypro.lessons.springboot.webcw.pojo.Status;
import ru.skypro.lessons.springboot.webcw.service.BidService;

@Repository
public interface LotRepository extends JpaRepository <Lot, Long> {
    Page<Lot> findByStatus(Status status, Pageable pageable);

}
