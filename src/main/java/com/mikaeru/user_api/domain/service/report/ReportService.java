package com.mikaeru.user_api.domain.service.report;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Classe que abstrai a criação de um relatório de usuários.
 * @author Mickael Luiz
 */
public interface ReportService extends Serializable {

    /**
     * Método responsável pela criação de um relatório de usuários simples.
     *
     * @param request {@link HttpServletRequest}
     * @return <code>{@link String}</code> base64
     */
    String generateReport(HttpServletRequest request);
}
