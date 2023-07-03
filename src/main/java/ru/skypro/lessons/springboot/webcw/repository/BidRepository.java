package ru.skypro.lessons.springboot.webcw.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.lessons.springboot.webcw.pojo.Bid;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository <Bid, Long> {

    Optional<Bid> findFirstByLotIdOrderByTimeStampAsc(Long lotId);

    @Query("SELECT b.bidderName FROM Bid b WHERE b.lotId = :lotId GROUP BY b.bidderName ORDER BY COUNT(b.bidderName) DESC")
    List<String> findMostFrequentBiddersByLotId(@Param("lotId") Long lotId);

    Optional<Bid> findFirstByLotIdOrderByTimeStampDesc(Long lotId);


    @Query("SELECT b FROM Bid b WHERE b.lot.id = :lotId ORDER BY b.timeStamp DESC")
    List <Bid> findLastBidByLotId(@Param("lotId") Long lotId, Pageable pageable);

}