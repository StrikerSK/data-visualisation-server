package com.charts.nivo.controller;

import com.charts.api.ticket.entity.TicketsParameters;
import com.charts.nivo.entity.NivoBubbleData;
import com.charts.nivo.entity.NivoLineData;
import com.charts.nivo.entity.NivoPieData;
import com.charts.nivo.service.NivoTicketsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nivo/ticket")
@AllArgsConstructor
public class NivoTicketController {

    private final NivoTicketsService nivoTicketsService;

    @GetMapping({"/line"})
    public List<NivoLineData> retrieveDynamicLineData(
            @RequestParam(required = false) List<Boolean> discounted,
            @RequestParam(required = false) List<String> month,
            @RequestParam(required = false) List<Integer> year,
            @RequestParam(required = false) List<String> ticket,
            @RequestParam String upperGroup,
            @RequestParam String lowerGroup
    ) {
        return nivoTicketsService.createDynamicLineData(upperGroup, lowerGroup, new TicketsParameters(month, year, discounted, ticket));
    }

    @GetMapping({"/pie"})
    public List<NivoPieData> retrieveDynamicPieData(
            @RequestParam(required = false) List<Boolean> discounted,
            @RequestParam(required = false) List<String> month,
            @RequestParam(required = false) List<Integer> year,
            @RequestParam(required = false) List<String> ticket,
            @RequestParam String group
    ) {
        return nivoTicketsService.createDynamicPieData(group, (new TicketsParameters(month, year, discounted, ticket)));
    }

    @GetMapping({"/bubble"})
    public NivoBubbleData retrieveBubbleData(
            @RequestParam(required = false) List<Boolean> discounted,
            @RequestParam(required = false) List<String> month,
            @RequestParam(required = false) List<Integer> year,
            @RequestParam(required = false) List<String> ticket,
            @RequestParam String upperGroup,
            @RequestParam String lowerGroup
    ) {
        return nivoTicketsService.createDynamicBubbleData(upperGroup, lowerGroup, new TicketsParameters(month, year, discounted, ticket));
    }

    @RequestMapping({"/bar"})
    public List<Map<String, Object>> retrieveDynamicBarData(
            @RequestParam(required = false) List<Boolean> discounted,
            @RequestParam(required = false) List<String> month,
            @RequestParam(required = false) List<Integer> year,
            @RequestParam(required = false) List<String> ticket,
            @RequestParam() String upperGroup,
            @RequestParam() String lowerGroup
    ) {
        return nivoTicketsService.createDynamicBarData(upperGroup, lowerGroup, new TicketsParameters(month, year, discounted, ticket));
    }

}
