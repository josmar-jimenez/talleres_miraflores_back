package com.inventory_system.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String name;
    @Column
    private String shortName;
    @Column
    private String barcode;
    @Column
    private BigDecimal price;
    @Column
    private BigDecimal cost;
    @Column
    private String image;
    /*Relations*/
    @ManyToOne(fetch = FetchType.EAGER)
    private Status status;
    @OneToMany(fetch = FetchType.EAGER)
    private List<ProductTag> tag;
}
