package com.charts.api.ticket.repository;

import com.charts.api.ticket.entity.v1.TicketEntityV1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public interface JpaTicketRepository extends JpaRepository<TicketEntityV1, Long> {
}
