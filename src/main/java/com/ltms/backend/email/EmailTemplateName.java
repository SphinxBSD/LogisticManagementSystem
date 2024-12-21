package com.ltms.backend.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    NOTICE_ACCOUNT("notice_account.html");
    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
