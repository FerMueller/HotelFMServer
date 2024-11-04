package com.hotelFM.service;

import com.hotelFM.model.Guest;
import com.hotelFM.model.Booking;
import com.hotelFM.repository.GuestRepository;
import com.hotelFM.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestService {

    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private BookingRepository reservaRepository;

    public List<Guest> listAllGuest() {
        return guestRepository.findAll();
    }

    public List<Guest> findInHotel() {
        return guestRepository.findByNoHotel(true);
    }

    public List<Guest> findByDocument(String document) {
        return guestRepository.findByDocumento(document);
    }

    public List<Guest> findByPhone(String phone) {
        return guestRepository.findByTelefone(phone);
    }

    public List<Guest> findByName(String name) {
        return guestRepository.findByNomeContaining(name);
    }

    public List<Guest> findByWithoutCheckin() {
        List<Booking> bookingWithoutCheckin = reservaRepository.findBydtCheckinIsNull();

        // Get guest that don't have check-in date
        List<Long> hospedeIds = bookingWithoutCheckin.stream()
                .map(Booking::getGuesId)
                .collect(Collectors.toList());

        // List of ids
        return guestRepository.findAllById(hospedeIds);
    }


    public void deleteGuest(Long id) {
        guestRepository.deleteById(id);
    }

    public Guest changeGuestStatus(Long idHospede, boolean noHotel) {
        Guest guest = guestRepository.findById(idHospede)
                .orElseThrow(() -> new RuntimeException("Hóspede não encontrado"));
        guest.setNoHotel(noHotel);
        return guestRepository.save(guest);
    }

    public Guest saveHospede(Guest guest) {
        return guestRepository.save(guest);
    }
}
