package com.komputasilayanan.tiketkereta.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "train_schedules")
public class TrainSchedule {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    private Integer price;

    private Integer capacity;

    @Column(name = "depart_at")
    private java.sql.Timestamp departAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "origin_id", nullable = false)
    private City origin;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "destination_id", nullable = false)
    private City destination;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "trainSchedule")
    private List<Passenger> passengers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Timestamp getDepartAt() {
        return departAt;
    }

    public void setDepartAt(Timestamp departAt) {
        this.departAt = departAt;
    }

    public City getOrigin() {
        return origin;
    }

    public void setOrigin(City origin) {
        this.origin = origin;
    }

    public City getDestination() {
        return destination;
    }

    public void setDestination(City destination) {
        this.destination = destination;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}
