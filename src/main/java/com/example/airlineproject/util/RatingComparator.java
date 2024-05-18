package com.example.airlineproject.util;

import com.example.airlineproject.entity.Company;

import java.util.Comparator;

public class RatingComparator implements Comparator<Company> {


        @Override
        public int compare(Company company1, Company company2) {
            return Double.compare(company2.getRating(), company1.getRating());
        }


}
