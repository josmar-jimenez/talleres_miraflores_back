package com.inventory_system.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String nick;
    @Column
    private String name;
    @Column
    private String cellphone;
    @Column
    private String address;
    @Column
    private String email;
    @Column
    private String password;
    @Column(name="emergency_phone")
    private String emergencyPhone;
    @Column(name="emergency_contact")
    private String emergencyContact;
    /*Relations*/
    @ManyToOne(fetch = FetchType.EAGER)
    private Store store;
    @ManyToOne(fetch = FetchType.EAGER)
    private Status status;
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;
}
