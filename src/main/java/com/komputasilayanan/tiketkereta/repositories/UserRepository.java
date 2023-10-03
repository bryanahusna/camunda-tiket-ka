package com.komputasilayanan.tiketkereta.repositories;

import com.komputasilayanan.tiketkereta.models.City;
import com.komputasilayanan.tiketkereta.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

}
