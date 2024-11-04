package com.hotelFM.repository;
import java.util.List;
import com.hotelFM.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBydtCheckinIsNull();
}
