package com.hotelFM.repository;
import com.hotelFM.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByNomeContaining(String nome);
    List<Guest> findByDocumento(String documento);
    List<Guest> findByTelefone(String telefone);
    List<Guest> findByNoHotel(boolean noHotel);

}
