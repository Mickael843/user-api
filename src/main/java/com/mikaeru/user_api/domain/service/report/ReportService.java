package com.mikaeru.user_api.domain.service.report;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public interface ReportService extends Serializable {

    String generateReport(HttpServletRequest request);
}
