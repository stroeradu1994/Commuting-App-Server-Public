package com.commuting.commutingapp.report.repo;

import com.commuting.commutingapp.report.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepo extends MongoRepository<Report, String> {
}
