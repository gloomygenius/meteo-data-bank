package com.mdbank.repository;

import com.mdbank.model.UpdateProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.MANDATORY)
public interface UpdateProcessRepository extends JpaRepository<UpdateProcess, Long> {
}
