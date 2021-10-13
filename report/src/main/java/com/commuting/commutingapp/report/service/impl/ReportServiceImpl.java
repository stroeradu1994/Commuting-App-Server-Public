package com.commuting.commutingapp.report.service.impl;

import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.report.model.Report;
import com.commuting.commutingapp.report.repo.ReportRepo;
import com.commuting.commutingapp.report.service.ReportService;
import com.commuting.commutingapp.security.model.Account;
import com.commuting.commutingapp.security.repo.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportRepo reportRepo;

    @Override
    public void createReport(String text) {
        String userId = SecurityUtils.getUserId();

        reportRepo.save(new Report(text, userId));
    }
}
