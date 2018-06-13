package com.fluig.companysearch.service;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fluig.companysearch.domain.Companies;
import com.fluig.companysearch.domain.Company;
import com.fluig.companysearch.domain.CompanyDetails;
import com.fluig.companysearch.domain.CompanyIndex;
import com.fluig.companysearch.domain.CompanyStatus;
import com.fluig.companysearch.domain.IndexType;
import com.fluig.companysearch.domain.Ranking;
import com.fluig.companysearch.domain.Rankings;

@Service
public class CompanySearchService {

    public static final String SEARCH_URL = "https://iosearch.reclameaqui.com.br/raichu-io-site-search-0.0.1-SNAPSHOT/companies/search/";
    public static final String DETAILS_URL = "https://iosite.reclameaqui.com.br/raichu-io-site-0.0.1-SNAPSHOT/company/{id}/public";
    private RestTemplate restTemplate = new RestTemplate();

    public List<Company> search(String name) {
        ResponseEntity<Companies> response = restTemplate.getForEntity(SEARCH_URL + name, Companies.class);
        return response.getBody().companies;
    }

    public List<CompanyIndex> get(String id) {
        ResponseEntity<CompanyDetails> response = null;
        try {
            response = restTemplate.getForEntity(DETAILS_URL, CompanyDetails.class, id);
        }catch (Exception e){ }
        if (response == null){
            return Collections.emptyList();
        }
        CompanyDetails indexes = response.getBody();
        return indexes.companyIndexes.stream()
                .map(json -> getIndex(json))
                .collect(Collectors.toList());
    }

    private CompanyIndex getIndex(String object) {
        List<String> values = Arrays.asList(object.replace("{", "")
                .replace("}", "")
                .split(","));
        CompanyIndex companyIndex = new CompanyIndex();
        companyIndex.id = getString("id", values);
        parseDoubleValues(values, companyIndex);
        parseLongValues(values, companyIndex);
        companyIndex.status = getStatus(values);
        companyIndex.type = getType(values);
        return companyIndex;

    }

    private void parseLongValues(List<String> values, CompanyIndex companyIndex) {
        companyIndex.totalAnswered = getLong("totalAnswered", values);
        companyIndex.totalComplains = getLong("totalComplains", values);
        companyIndex.totalComplains30 = getLong("totalComplains30", values);
        companyIndex.totalEvaluated = getLong("totalEvaluated", values);
        companyIndex.totalNotAnswered = getLong("totalNotAnswered", values);
    }

    private void parseDoubleValues(List<String> values, CompanyIndex companyIndex) {
        companyIndex.answeredPercentual = getDouble("answeredPercentual", values);
        companyIndex.averageAnswerTime = getDouble("averageAnswerTime", values);
        companyIndex.averageAnswerTime3M = getDouble("averageAnswerTime3M", values);
        companyIndex.consumerScore = getDouble("consumerScore", values);
        companyIndex.dealAgainPercentual = getDouble("dealAgainPercentual", values);
        companyIndex.finalScore = getDouble("finalScore", values);
        companyIndex.solvedPercentual = getDouble("solvedPercentual", values);
    }


    private IndexType getType(List<String> values) {
        return IndexType.getIndexByType(getString("type", values));
    }

    private CompanyStatus getStatus(List<String> values) {
        return CompanyStatus.getCompanyStatusByType(getString("status", values));
    }

    private Long getLong(String key, List<String> values) {
        return Long.valueOf(getString(key, values).trim());
    }

    private Double getDouble(String key, List<String> values) {
        return Double.valueOf(getString(key, values));
    }

    private String getString(String key, List<String> values) {
        return values.stream().filter(v -> v.trim().startsWith(key))
                .map(v -> getValue(key, v))
                .findFirst()
                .orElse(null);
    }

    private String getValue(String key, String v) {
        return v.replace(key + "=", "");
    }

    public Rankings getRankings(List<Company> companies) {
        Rankings rankings = new Rankings();
        for (Company company : companies) {
            List<CompanyIndex> companyIndices = get(company.id);
            Ranking ranking = calculateRanking(companyIndices, company);
            rankings.rankingList.add(ranking);
        }
        return rankings;


    }

    private Ranking calculateRanking(List<CompanyIndex> companyIndices, Company company) {
        Ranking ranking = new Ranking();
        ranking.company = company;

        int count = 0;
        double totalPercentual = 0.0;
        double finalGrade = 0.0;

        for (CompanyIndex companyIndex : companyIndices) {
            if (companyIndex.type != null
                    && (companyIndex.type == IndexType.SIX_MONTHS || companyIndex.type == IndexType.LAST_YEAR)) {
                totalPercentual = totalPercentual + this.calculateRankingByType(companyIndex);
                count++;
            }
        }

        if (count > 0) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            try {
                finalGrade = decimalFormat.parse(decimalFormat.format((totalPercentual / count) / 10)).doubleValue();
            } catch (Exception e) {}
        }

        ranking.grade = finalGrade;

        return ranking;
    }

    private double calculateRankingByType(CompanyIndex companyIndex) {
        double answeredPercentual = companyIndex.answeredPercentual;
        double solvedPercentual = companyIndex.solvedPercentual;
        double dealAgainPercentual = companyIndex.dealAgainPercentual;
        double score = (companyIndex.finalScore*100)/5;

        switch (companyIndex.type) {
            case SIX_MONTHS:
                return (answeredPercentual * 0.4)
                        + (solvedPercentual * 0.3)
                        + (dealAgainPercentual * 0.2)
                        + (score * 0.1);

            case LAST_YEAR:
                return (dealAgainPercentual * 0.4)
                        + (score * 0.3)
                        + (answeredPercentual * 0.2)
                        + (solvedPercentual * 0.1);
        }

        return 0.0;
    }
}
