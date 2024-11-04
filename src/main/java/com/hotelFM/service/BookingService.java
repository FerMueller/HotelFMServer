package com.hotelFM.service;

import com.hotelFM.model.Booking;
import com.hotelFM.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository reservaRepository;
    @Autowired
    private GuestService guestService;

    public Booking createReserva(Booking booking) {
        return reservaRepository.save(booking);
    }

    public List<Booking> findBookings() {
        return reservaRepository.findAll();
    }


    public Optional<Booking> findBookingId(Long id) {
        return reservaRepository.findById(id);
    }

    public void deleteReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    public Booking doCheckin(Long id) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalTime checkinTime = LocalTime.of(14, 0);

        Booking booking = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
        LocalDateTime dtStartBooking = booking.getDtStartBooking();

        if (currentTime.toLocalDate().isBefore(dtStartBooking.toLocalDate())) {
            throw new IllegalStateException("A data de reserva deve ser menor ou igual à data atual.");
        }
        // Verificar se o check-in está sendo realizado antes do horário permitido (14h00)
        else if (currentTime.toLocalTime().isBefore(checkinTime) && currentTime.toLocalDate().isEqual(dtStartBooking.toLocalDate())) {
            throw new IllegalStateException("O horário para a realização do check-in será a partir das 14h00min.");
        }

        booking.setDtCheckin(LocalDateTime.now()); // Define a data de check-in como o momento atual
        guestService.changeGuestStatus(booking.getGuesId(), true);

        return reservaRepository.save(booking);
    }

    public Booking doCheckout(Long id) {
        Booking booking = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        booking.setDtCheckout(LocalDateTime.now()); // Define a data de checkout como o momento atual

        // Cálculo do valor total com base nas regras de negócio
        double valorTotal = calculateTotalAmount(booking);
        booking.setQtAccValue(valorTotal);
        guestService.changeGuestStatus(booking.getGuesId(), false);
        return reservaRepository.save(booking);
    }

    private double calculateLateCheckoutFee(LocalDateTime end) {
        if (end.toLocalTime().isAfter(LocalTime.of(12, 0))) {
            double dailyRate = (end.getDayOfWeek() == DayOfWeek.SATURDAY || end.getDayOfWeek() == DayOfWeek.SUNDAY) ? 180 : 120;
            return dailyRate * 0.5; // 50% of daily rate fee
        }
        return 0;
    }

    private double calculateDailyRate(DayOfWeek day, boolean usesCar) {
        double dailyRate = (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) ? 180 : 120;
        double parkingFee = (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) ? 20 : 15;
        return dailyRate + (usesCar ? parkingFee : 0);
    }

    public double calculateTotalAmount(Booking reservation) {
        LocalDateTime start = reservation.getDtCheckin();
        LocalDateTime end = reservation.getDtCheckout();
        boolean usesCar = reservation.isHasCar();

        double totalAmount = 0;

        while (!start.isAfter(end)) {
            totalAmount += calculateDailyRate(start.getDayOfWeek(), usesCar);
            start = start.plusDays(1); // Advance to the next day
        }

        // Check and add additional fee if check-out is after 12:00 PM
        totalAmount += calculateLateCheckoutFee(end);

        return totalAmount;
    }
}
