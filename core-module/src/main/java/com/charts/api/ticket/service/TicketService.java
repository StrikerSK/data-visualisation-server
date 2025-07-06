package com.charts.api.ticket.service;

import com.charts.api.ticket.entity.enums.TicketType;
import com.charts.general.entity.GroupingEntity;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.api.ticket.repository.JpaTicketV2Repository;
import com.charts.api.ticket.entity.TicketsParameters;
import com.charts.general.entity.enums.types.EnumAdapter;
import com.charts.general.entity.enums.types.Months;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final JpaTicketV2Repository ticketRepository;

    public TicketService(JpaTicketV2Repository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void saveAll(List<UpdateTicketEntity> ticketEntityList) { ticketRepository.saveAll(ticketEntityList); }

    public List<UpdateTicketEntity> findAll(Integer size) {
        Pageable pageable = Pageable.ofSize(size);
        return ticketRepository.findAll(pageable).getContent();
    }

    public List<UpdateTicketEntity> getAllByFilter(TicketsParameters parameters) {
        return ticketRepository.findAllByMonthInAndYearInAndTicketTypeInAndDiscountedIn(
                parameters.getMonths(),
                parameters.getYearInteger(),
                parameters.getTicketType(),
                parameters.getDiscounted()
        );
    }

    public List<GroupingEntity<TicketType>> getTicketsByTicketType(TicketsParameters parameters) {
        return ticketRepository.findGroupedByTicketType(
                parameters.getMonths(),
                parameters.getDiscounted(),
                parameters.getTicketType(),
                parameters.getYearInteger()
        );
    }

    public List<GroupingEntity<Months>> getTicketsByMonth(TicketsParameters parameters) {
        return ticketRepository.findGroupedByMonth(
                parameters.getMonths(),
                parameters.getDiscounted(),
                parameters.getTicketType(),
                parameters.getYearInteger()
        );
    }

    public List<GroupingEntity<EnumAdapter>> getTicketsByYear(TicketsParameters parameters) {
        return ticketRepository.findGroupedByYear(
                parameters.getMonths(),
                parameters.getDiscounted(),
                parameters.getTicketType(),
                parameters.getYearInteger()
        )
                .stream()
                .map(e -> new GroupingEntity<>(new EnumAdapter(e.getKey()), e.getValue()))
                .collect(Collectors.toList());
    }

    public List<GroupingEntity<EnumAdapter>> getTicketsByDiscounted(TicketsParameters parameters) {
        return ticketRepository.findGroupedByDiscounted(
                parameters.getMonths(),
                parameters.getDiscounted(),
                parameters.getTicketType(),
                parameters.getYearInteger()
        )
                .stream()
                .map(e -> new GroupingEntity<>(new EnumAdapter(e.getKey().toString(), 0), e.getValue()))
                .collect(Collectors.toList());
    }

}
