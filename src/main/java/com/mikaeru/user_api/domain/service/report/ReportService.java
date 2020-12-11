package com.mikaeru.user_api.domain.service.report;

import com.mikaeru.user_api.dto.request.user.ReportParam;
import net.sf.jasperreports.engine.JRException;

import javax.servlet.ServletContext;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;

public interface ReportService extends Serializable {

    public byte[] generateReport(String nameReport, ServletContext servletContext) throws SQLException, JRException;

    public byte[] generateReport(String nameReport, ServletContext servletContext, ReportParam param) throws SQLException, JRException, ParseException;
}
