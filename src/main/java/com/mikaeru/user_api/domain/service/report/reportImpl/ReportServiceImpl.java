package com.mikaeru.user_api.domain.service.report.reportImpl;

import com.mikaeru.user_api.domain.service.report.ReportService;
import com.mikaeru.user_api.dto.user.in.ReportParam;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ReportServiceImpl implements ReportService {

    private static final long serialVersionUID = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public byte[] generateReport(String nameReport, ServletContext servletContext) throws SQLException, JRException {
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

    @Override
    public byte[] generateReport(String nameReport, ServletContext servletContext, ReportParam param) throws SQLException, JRException, ParseException {
        Map<String, Object> params = getFormattedDateParameters(param);
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            String pathJasper = servletContext.getRealPath("/report") + File.separator + nameReport + ".jasper";

            JasperPrint print = JasperFillManager.fillReport(pathJasper, params, connection);

            return JasperExportManager.exportReportToPdf(print);
        }
    }

    private Map<String, Object> getFormattedDateParameters(ReportParam param) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = format.format(dateFormat.parse(param.getStartDate()));
        String endDate = format.format(dateFormat.parse(param.getEndDate()));
        Map<String, Object> params = new HashMap<>();
        params.put("START_DATE", startDate);
        params.put("END_DATE", endDate);
        return params;
    }
}
