package com.komputasilayanan.tiketkereta.repositories;

import com.komputasilayanan.tiketkereta.models.City;
import com.komputasilayanan.tiketkereta.models.Train;
import com.komputasilayanan.tiketkereta.models.TrainSchedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TrainScheduleRepository extends CrudRepository<TrainSchedule, Integer> {
    @Query("SELECT ts from TrainSchedule ts WHERE ts.origin = ?1 AND ts.destination = ?2 AND DATE(ts.departAt) = ?3")
    List<TrainSchedule> findSchedule(City origin, City destination, java.sql.Date departAt);
}
