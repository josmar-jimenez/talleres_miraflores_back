package com.inventory_system.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String shortName;
    @Column
    private String name;
    @Column
    private String address;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private String image;
    /*Relations*/
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Status status;
}
