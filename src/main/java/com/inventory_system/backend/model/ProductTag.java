package com.inventory_system.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductTag {

    @Id
    @Column
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;
}
