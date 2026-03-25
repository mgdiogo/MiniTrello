package com.mgdiogo.minitrello.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mgdiogo.minitrello.entities.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {}
