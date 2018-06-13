package com.fluig.companysearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyIndex {

    public String id;
    public Double averageAnswerTime;
    public Double finalScore;
    public Long totalNotAnswered;
    public Long totalComplains;
    public Double consumerScore;
    public Double solvedPercentual;
    public IndexType type;
    public Long totalAnswered;
    public Long totalComplains30;
    public Double averageAnswerTime3M;
    public Double dealAgainPercentual;
    public Long totalEvaluated;
    public Double answeredPercentual;
    public CompanyStatus status;
    public Company company;

}
