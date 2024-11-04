package com.hotelFM.controller;

import com.hotelFM.model.Booking;
import com.hotelFM.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    public void testCreateReserva() throws Exception {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setDtStartBooking(LocalDateTime.now());
        booking.setDtEndBooking(LocalDateTime.now().plusDays(3));

        when(bookingService.createReserva(any(Booking.class))).thenReturn(booking);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking")
                        .content("{\"hospedeId\": 1, \"dtReservaInicio\": \"2024-09-20T10:00:00\", \"dtReservaFim\": \"2024-09-23T10:00:00\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(bookingService, times(1)).createReserva(any(Booking.class));
    }

    @Test
    public void testRealizarCheckin() throws Exception {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setDtCheckin(LocalDateTime.now());

        when(bookingService.doCheckin(1L)).thenReturn(booking);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking/1/checkin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.dtCheckin").isNotEmpty());

        verify(bookingService, times(1)).doCheckin(1L);
    }

    @Test
    public void testRealizarCheckout() throws Exception {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setDtCheckout(LocalDateTime.now());

        when(bookingService.doCheckout(1L)).thenReturn(booking);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking/1/checkout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.dtCheckout").isNotEmpty());

        verify(bookingService, times(1)).doCheckout(1L);
    }

    @Test
    public void testfindBookingId() throws Exception {
        Booking booking = new Booking();
        booking.setId(1L);

        when(bookingService.findBookingId(1L)).thenReturn(Optional.of(booking));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/booking/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(bookingService, times(1)).findBookingId(1L);
    }
}
