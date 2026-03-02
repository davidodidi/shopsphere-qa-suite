package com.shopsphere.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private int stockQuantity;
    private String imageUrl;
    private double rating;
    private int reviewCount;
}
