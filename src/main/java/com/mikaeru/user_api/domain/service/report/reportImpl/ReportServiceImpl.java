package com.mikaeru.user_api.domain.service.report.reportImpl;

import com.mikaeru.user_api.domain.service.report.ReportService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired private JdbcTemplate jdbcTemplate;

    @Override
    public String generateReport(HttpServletRequest request) {
        String pdfBase64 = null;

        try {

            byte[] pdf = generateReport("user-report", request.getServletContext());
            pdfBase64 = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

        } catch (SQLException | JRException e) {
            // TODO Tratar essa exceção
            e.printStackTrace();
        }

        return pdfBase64;
    }

    private byte[] generateReport(String nameReport, ServletContext servletContext) throws SQLException, JRException {
        //Obtendo conexão com o banco de dados
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            //Carregar o caminho do arquivo jasper
            String pathJasper = servletContext.getRealPath("/report") + File.separator + nameReport + ".jasper";

            //Gerar relatório com os dados e conexão
            JasperPrint print = JasperFillManager.fillReport(pathJasper, new HashMap<>(), connection);

            //Exporta para byte o PDF para fazer o download
            return JasperExportManager.exportReportToPdf(print);
        }
    }
}
