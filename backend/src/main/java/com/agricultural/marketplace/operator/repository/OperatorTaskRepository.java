package com.agricultural.marketplace.operator.repository;

import java.util.List;

import com.agricultural.marketplace.operator.entity.OperatorTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorTaskRepository extends JpaRepository<OperatorTask, Long> {

    List<OperatorTask> findByAssigneeId(Long assigneeId);

    Page<OperatorTask> findByStatus(String status, Pageable pageable);

    Page<OperatorTask> findByTaskType(String taskType, Pageable pageable);
}

