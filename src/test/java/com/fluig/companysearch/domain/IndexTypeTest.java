package com.fluig.companysearch.domain;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;

public class IndexTypeTest {

    @Test
    public void whenValidIndexType() {
        Assert.assertThat(IndexType.getIndexByType("SIX_MONTHS"), equalTo(IndexType.SIX_MONTHS));
    }

    @Test
    public void whenInvalidIndexType() {
        Assert.assertThat(IndexType.getIndexByType("adssdaasdsa"), nullValue());
    }

    @Test
    public void whenNullIndexType() {
        Assert.assertThat(IndexType.getIndexByType(null), nullValue());
    }
}
