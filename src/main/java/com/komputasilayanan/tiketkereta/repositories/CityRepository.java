package com.komputasilayanan.tiketkereta.repositories;

import com.komputasilayanan.tiketkereta.models.City;
import com.komputasilayanan.tiketkereta.models.TrainSchedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Integer> {
    @Query("SELECT c FROM City c WHERE LOWER(c.name) LIKE LOWER(?1)")
    List<City> findByName(String name);
}
