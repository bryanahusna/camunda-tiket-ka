package com.komputasilayanan.tiketkereta;

import com.komputasilayanan.tiketkereta.models.Passenger;
import com.komputasilayanan.tiketkereta.repositories.CityRepository;
import com.komputasilayanan.tiketkereta.repositories.PassengerRepository;
import com.komputasilayanan.tiketkereta.repositories.TrainScheduleRepository;
import com.komputasilayanan.tiketkereta.repositories.UserRepository;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
public class PaymentService {
    @Autowired
    private CityRepository cityRepo;

    @Autowired
    private TrainScheduleRepository tsRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PassengerRepository passengerRepo;

    @JobWorker(type = "createPaymentBill", autoComplete = true)
    public Map<String, Object> createPaymentBill(@Variable Integer passenger_id){
        // Response variable
        HashMap<String, Object> variables = new HashMap<>();

        // Return success
        variables.put("createPaymentSuccess", true);
        return variables;
    }

    @JobWorker(type = "confirmPayment", autoComplete = true)
    public Map<String, Object> confirmPayment(@Variable Integer passenger_id){
        // Response variable
        HashMap<String, Object> variables = new HashMap<>();

        // Set payment success
        Optional<Passenger> passengerOpt = passengerRepo.findById(passenger_id);
        if (passengerOpt.isPresent()) {
            Passenger passenger = passengerOpt.get();
            passenger.setPaidAt(new Timestamp(System.currentTimeMillis()));
            passengerRepo.save(passenger);
        }

        // Return success
        variables.put("confirmPaymentSuccess", true);
        return variables;
    }
}
