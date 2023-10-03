package com.komputasilayanan.tiketkereta.repositories;

import com.komputasilayanan.tiketkereta.models.Passenger;
import com.komputasilayanan.tiketkereta.models.User;
import org.springframework.data.repository.CrudRepository;

public interface PassengerRepository extends CrudRepository<Passenger, Integer> {

}
