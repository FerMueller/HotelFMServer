package com.hotelFM.service;

import com.hotelFM.model.Booking;
import com.hotelFM.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @MockBean
    private BookingRepository reservaRepository;

    @MockBean
    private GuestService guestService;

    @Test
    public void testCreateBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setDtStartBooking(LocalDateTime.now());
        booking.setDtEndBooking(LocalDateTime.now().plusDays(3));

        when(reservaRepository.save(any(Booking.class))).thenReturn(booking);

        Booking booking1 = bookingService.createReserva(booking);

        assertEquals(1L, booking1.getId());
        verify(reservaRepository, times(1)).save(booking);
    }

    @Test
    public void testDoCheckin() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setGuesId(1L);
        booking.setDtStartBooking(LocalDateTime.now());
        booking.setDtEndBooking(LocalDateTime.now().plusDays(3));

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(reservaRepository.save(any(Booking.class))).thenReturn(booking);

        Booking bookingComCheckin = bookingService.doCheckin(1L);

        assertNotNull(bookingComCheckin.getDtCheckin());
        verify(guestService, times(1)).changeGuestStatus(1L, true);
        verify(reservaRepository, times(1)).save(bookingComCheckin);
    }

    @Test
    public void testDoCheckout() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setGuesId(1L);
        booking.setDtStartBooking(LocalDateTime.now());
        booking.setDtEndBooking(LocalDateTime.now().plusDays(3));
        booking.setDtCheckin(LocalDateTime.now().minusDays(2));
        booking.setHasCar(false);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(reservaRepository.save(any(Booking.class))).thenReturn(booking);

        Booking bookingComCheckout = bookingService.doCheckout(1L);

        assertNotNull(bookingComCheckout.getDtCheckout());
        assertEquals(540, bookingComCheckout.getQtAccValue());
        verify(guestService, times(1)).changeGuestStatus(1L, false);
        verify(reservaRepository, times(1)).save(bookingComCheckout);
    }

    @Test
    public void testCalculateTotalAmount() {
        Booking booking = new Booking();
        booking.setDtCheckin(LocalDateTime.of(2024, 9, 20, 12, 0));
        booking.setDtCheckout(LocalDateTime.of(2024, 9, 23, 12, 0));
        booking.setHasCar(true);

        double valorTotal = bookingService.calculateTotalAmount(booking);

        assertEquals(670, valorTotal);
    }
}
