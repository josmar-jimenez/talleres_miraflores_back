package com.inventory_system.backend.model;

import com.inventory_system.backend.enums.Allowed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleAction {

    @Id
    @Column
    private Integer id;
    @Column
    @Enumerated(EnumType.STRING)
    private Allowed allowed;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Role role;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Operative operative;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Action action;
}
