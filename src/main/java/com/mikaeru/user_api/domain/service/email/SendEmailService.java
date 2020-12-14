package com.mikaeru.user_api.domain.service.email;

import com.mikaeru.user_api.domain.handler.Problem;

public interface SendEmailService {
    Problem sendRecoveryEmail(String username);
}
