package com.komputasilayanan.tiketkereta.services;

import com.komputasilayanan.tiketkereta.models.*;
import com.komputasilayanan.tiketkereta.repositories.*;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TrainService {
    @Autowired
    private ZeebeClient client;

    @Autowired
    private CityRepository cityRepo;

    @Autowired
    private TrainScheduleRepository tsRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PassengerRepository passengerRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @JobWorker(type = "getTrainDetail", autoComplete = true)
    public Map<String, Object> getTrainDetail(@Variable String origin, @Variable String destination, @Variable String departure_date){
        // Response variable
        HashMap<String, Object> variables = new HashMap<>();

        // 1. get city
        List<City> originCities = cityRepo.findByName(origin);
        List<City> destinationCities = cityRepo.findByName(destination);
        if (originCities.size() == 0 || destinationCities.size() == 0) {
            variables.put("trainExists", false);
            return variables;
        }
        City originCity = originCities.get(0);
        City destinationCity = destinationCities.get(0);
        if (Objects.equals(originCity.getId(), destinationCity.getId())) {
            variables.put("trainExists", false);
            return variables;
        }

        // 2. get schedule
        List<TrainSchedule> trainSchedules = tsRepo.findSchedule(originCity, destinationCity, java.sql.Date.valueOf(departure_date));
        if (trainSchedules.size() == 0) {
            variables.put("trainExists", false);
            return variables;
        }
        TrainSchedule trainSchedule = null;
        for (TrainSchedule ts: trainSchedules) {
            if (ts.getPassengers().size() < ts.getCapacity()) {
                trainSchedule = ts;
                break;
            }
        }

        if (trainSchedule == null) {
            variables.put("trainExists", false);
            return variables;
        }

        // 3. return schedule
        variables.put("trainExists", true);
        variables.put("train_schedule_id", trainSchedule.getId());
        variables.put("train_name", trainSchedule.getTrain().getName());
        variables.put("train_price", trainSchedule.getPrice());
        variables.put("train_seat_available", trainSchedule.getCapacity() - trainSchedule.getPassengers().size());

        return variables;
    }

    @JobWorker(type = "checkAndLockTrain", autoComplete = true)
    public Map<String, Object> checkAndLockTrain(@Variable Integer train_schedule_id, @Variable String name){
        // Response variable
        HashMap<String, Object> variables = new HashMap<>();

        // 1. Check if schedule available
        Optional<TrainSchedule> trainScheduleOpt = tsRepo.findById(train_schedule_id);
        if (trainScheduleOpt.isEmpty()) {
            variables.put("seatAvailable", false);
            return variables;
        }
        TrainSchedule trainSchedule = trainScheduleOpt.get();
        if (trainSchedule.getPassengers().size() >= trainSchedule.getCapacity()) {
            variables.put("seatAvailable", false);
            return variables;
        }

        // 2. Add passenger
        User user = new User();
        user.setName(name);
        userRepo.save(user);

        Passenger passenger = new Passenger();
        passenger.setUser(user);
        passenger.setTrainSchedule(trainSchedule);
        passengerRepo.save(passenger);

        // 3. Return response
        variables.put("seatAvailable", true);

        variables.put("passenger_id", passenger.getId());
        return variables;
    }

    @JobWorker(type = "requestPaymentBill", autoComplete = true)
    public void requestPaymentBill(@Variable Integer passenger_id){
        client.newPublishMessageCommand()
                .messageName("RequestPaymentBill")
                .correlationKey("1")
                .send();
    }

    @JobWorker(type = "receivePaymentBill", autoComplete = true)
    public Map<String, Object> receivePaymentBill(@Variable Integer passenger_id, @Variable Integer paymentId){
        // Response variable
        HashMap<String, Object> variables = new HashMap<>();

        // 1. Get passenger
        Optional<Passenger> passengerOpt = passengerRepo.findById(passenger_id);
        if (passengerOpt.isEmpty()) {
            return variables;
        }
        Passenger passenger = passengerOpt.get();

        // 2. Get payment
        Optional<Payment> paymentOpt = paymentRepo.findById(paymentId);
        if (paymentOpt.isEmpty()) {
            return variables;
        }
        Payment payment = paymentOpt.get();

        // 3. Update passenger
        passenger.setPayment(payment);
        passengerRepo.save(passenger);

        // 4. Set and return variables
        variables.put("paymentId", paymentId);
        return variables;
    }

    @JobWorker(type = "forwardPayment", autoComplete = true)
    public void forwardPayment(@Variable Integer passenger_id, @Variable Integer paymentId){
        client.newPublishMessageCommand()
                .messageName("Pay")
                .correlationKey("1")
                .send();
    }
}
