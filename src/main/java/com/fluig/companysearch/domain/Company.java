package com.fluig.companysearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {

    public String id;
    public String companyName;
    // pra que serve?
    public Integer count;
    public String fantasyName;
    public String shortname;
}
