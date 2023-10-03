package com.komputasilayanan.tiketkereta.repositories;

import com.komputasilayanan.tiketkereta.models.UserEx;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserExRepository extends CrudRepository<UserEx, Integer> {

}
