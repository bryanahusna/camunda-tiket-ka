package com.komputasilayanan.tiketkereta.services;

import com.komputasilayanan.tiketkereta.models.Passenger;
import com.komputasilayanan.tiketkereta.models.Payment;
import com.komputasilayanan.tiketkereta.repositories.*;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
public class PaymentService {
    @Autowired
    private ZeebeClient client;

    @Autowired
    private PaymentRepository paymentRepo;

    @JobWorker(type = "createPaymentBill", autoComplete = true)
    public Map<String, Object> createPaymentBill(){
        // Response variable
        HashMap<String, Object> variables = new HashMap<>();

        // 1. create payment
        Payment payment = new Payment();
        payment.setStatus("UNPAID");
        payment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        payment = paymentRepo.save(payment);

        // Return success
        variables.put("paymentId", payment.getId());
        client.newPublishMessageCommand()
                .messageName("PaymentBillCreated")
                .correlationKey("1")
                .variables(variables)
                .send();

        return variables;
    }

    @JobWorker(type = "confirmPayment", autoComplete = true)
    public void confirmPayment(@Variable Integer paymentId){
        Optional<Payment> paymentOpt = paymentRepo.findById(paymentId);
        if (paymentOpt.isEmpty()) {
            return;
        }

        Payment payment = paymentOpt.get();
        payment.setStatus("PAID");
        payment.setPaidAt(new Timestamp(System.currentTimeMillis()));
        paymentRepo.save(payment);

        client.newPublishMessageCommand()
                .messageName("PaymentSuccess")
                .correlationKey("1")
                .send();
    }
}
