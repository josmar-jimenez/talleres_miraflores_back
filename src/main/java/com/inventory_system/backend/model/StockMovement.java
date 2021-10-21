package com.inventory_system.backend.model;

import com.inventory_system.backend.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    @Enumerated(EnumType.STRING)
    private MovementType movementType;
    @Column
    private Integer cant;
    /*Relations*/
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
    @ManyToOne(fetch = FetchType.EAGER)
    private Store sourceStore;
    @ManyToOne(fetch = FetchType.EAGER)
    private Store destinyStore;
}
