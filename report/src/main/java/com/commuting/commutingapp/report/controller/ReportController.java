package com.commuting.commutingapp.report.controller;

import com.commuting.commutingapp.report.dto.CreateReport;
import com.commuting.commutingapp.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @PostMapping()
    public ResponseEntity<?> createReport(@RequestBody CreateReport createReport) {
        reportService.createReport(createReport.getText());
        return ResponseEntity.ok().build();
    }
}