package com.fluig.companysearch.domain;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;

public class CompanyStatusTest {

    @Test
    public void whenValidCompanyStatus() {
        Assert.assertThat(CompanyStatus.getCompanyStatusByType("REGULAR"), equalTo(CompanyStatus.REGULAR));
    }

    @Test
    public void whenInvalidCompanyStatus() {
        Assert.assertThat(CompanyStatus.getCompanyStatusByType("adssdaasdsa"), nullValue());
    }

    @Test
    public void whenNullCompanyStatus() {
        Assert.assertThat(CompanyStatus.getCompanyStatusByType(null), nullValue());
    }
}
