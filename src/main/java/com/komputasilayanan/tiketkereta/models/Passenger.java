package com.komputasilayanan.tiketkereta.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "passengers")
public class Passenger {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = true)
    private Payment payment;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "train_schedule_id", nullable = false)
    private TrainSchedule trainSchedule;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TrainSchedule getTrainSchedule() {
        return trainSchedule;
    }

    public void setTrainSchedule(TrainSchedule trainSchedule) {
        this.trainSchedule = trainSchedule;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
