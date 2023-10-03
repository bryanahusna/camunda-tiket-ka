package com.komputasilayanan.tiketkereta.repositories;

import com.komputasilayanan.tiketkereta.models.City;
import com.komputasilayanan.tiketkereta.models.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
}
