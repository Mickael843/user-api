package com.mikaeru.user_api.domain.service.report;

import com.mikaeru.user_api.dto.user.in.ReportParam;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public interface ReportService extends Serializable {

    String generateReport(HttpServletRequest request);

    String generateReport(ReportParam param, HttpServletRequest request);
}
