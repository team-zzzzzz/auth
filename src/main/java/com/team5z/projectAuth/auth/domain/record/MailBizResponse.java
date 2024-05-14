package com.team5z.projectAuth.auth.domain.record;

import java.util.List;

public record MailBizResponse (
        String resultCode,
        String resultMessage,
        List<MailBizVo> items
) {}
