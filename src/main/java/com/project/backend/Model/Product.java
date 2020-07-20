package com.project.backend.Model;

import org.springframework.data.relational.core.mapping.Table;

@Table("Product")
public class Product {
    int id;
    String detail;
    String[] portfolioImgLink;
}