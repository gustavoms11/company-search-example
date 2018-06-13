package com.fluig.companysearch.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluig.companysearch.domain.Companies;
import com.fluig.companysearch.domain.Company;
import com.fluig.companysearch.domain.CompanyDetails;
import com.fluig.companysearch.domain.CompanyIndex;
import com.fluig.companysearch.domain.Rankings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CompanySearchServiceTest {

    public static final String TIM_URL_LIST = "https://iosearch.reclameaqui.com.br/raichu-io-site-search-0.0.1-SNAPSHOT/companies/search/tim";
    public static final String TIM_DETAIL_URL = "https://iosite.reclameaqui.com.br/raichu-io-site-0.0.1-SNAPSHOT/company/{id}/public";

    @InjectMocks
    private CompanySearchService service;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenSearchByTimThenReturnManyResults() throws IOException {
        ResponseEntity<Companies> responseEntity = new ResponseEntity<Companies>(readCompanies("tim.json", Companies.class), HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(TIM_URL_LIST, Companies.class)).thenReturn(responseEntity);
        List<Company> companies = service.search("tim");
        Assert.assertTrue(companies != null);
    }

    @Test
    public void whenSearchByTimIDThenReturnTimDetails() throws IOException {
        ResponseEntity<CompanyDetails> responseEntity = new ResponseEntity<CompanyDetails>(readCompanies("timDetails.json", CompanyDetails.class), HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(TIM_DETAIL_URL, CompanyDetails.class,"2852")).thenReturn(responseEntity);
        List<CompanyIndex> indexes = service.get("2852");
        Assert.assertTrue(indexes != null);
    }

    @Test
    public void whenRankingTim() throws IOException {
        Company tim = new Company();
        tim.id = "2852";
        List<Company> companies = new ArrayList<>();
        companies.add(tim);
        ResponseEntity<CompanyDetails> responseEntity = new ResponseEntity<CompanyDetails>(readCompanies("timDetails.json", CompanyDetails.class), HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(TIM_DETAIL_URL, CompanyDetails.class,"2852")).thenReturn(responseEntity);
        Rankings rankings = service.getRankings(companies);

        Assert.assertTrue(rankings != null);
        Assert.assertThat(rankings.rankingList.size(), equalTo(1));
        Assert.assertThat(rankings.rankingList.get(0).grade, equalTo(2.49));
    }

    @Test
    public void whenGetNoResponse() throws Exception {
        Mockito.when(restTemplate.getForEntity(TIM_DETAIL_URL, CompanyDetails.class,"2852")).thenReturn(null);
        List<CompanyIndex> indexes = service.get("2852");

        Assert.assertEquals(0, indexes.size());
    }

    public <T> T readCompanies(String file, Class<T> type) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.getClass().getResourceAsStream(file), type);
    }

}