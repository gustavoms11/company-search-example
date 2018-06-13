package com.fluig.companysearch.api.v1.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fluig.companysearch.domain.Companies;
import com.fluig.companysearch.domain.Company;
import com.fluig.companysearch.domain.Rankings;
import com.fluig.companysearch.service.CompanySearchService;


@RestController
@RequestMapping("api/v1/companies")
public class CompanySearchController {

    private CompanySearchService companySearchService;

    public CompanySearchController(CompanySearchService companySearchService) {
        this.companySearchService = companySearchService;
    }

    @RequestMapping(value = "/{name}" ,method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Companies> findCompanies(@PathVariable(name = "name") String name) {
        return new ResponseEntity(companySearchService.search(name), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rankings> getRanking(@RequestBody List<Company> companies) {
        return new ResponseEntity<Rankings>(companySearchService.getRankings(companies), HttpStatus.OK);
    }


}
