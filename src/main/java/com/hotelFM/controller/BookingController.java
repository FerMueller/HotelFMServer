package com.hotelFM.controller;

import com.hotelFM.model.Booking;
import com.hotelFM.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createReserva(@RequestBody Booking booking) {
        Booking novaBooking = bookingService.createReserva(booking);
        return ResponseEntity.ok(novaBooking);
    }

    @PostMapping("/{id}/checkin")
    public ResponseEntity<Booking> doCheckin(@PathVariable Long id) {
        Booking booking = bookingService.doCheckin(id);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<Booking> realizarCheckout(@PathVariable Long id) {
        Booking booking = bookingService.doCheckout(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping
    public List<Booking> getBookins() {
        return bookingService.findBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> findBookingId(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.findBookingId(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteReserva(id);
        return ResponseEntity.noContent().build();
    }
}
