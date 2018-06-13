package com.fluig.companysearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public enum CompanyStatus {
    REGULAR,
    GOOD,
    BAD,
    NOT_RECOMMENDED,
    RECOMMENDED,
    NO_INDEX;


    public static CompanyStatus getCompanyStatusByType(String type) {
        try {
            return CompanyStatus.valueOf(type.trim());
        } catch (Exception iae) {
            return null;
        }
    }
}
