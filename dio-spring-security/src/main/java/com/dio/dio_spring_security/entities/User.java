package com.dio.dio_spring_security.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tab_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    // Define uma coleção de roles do usuário
    @ElementCollection(fetch = FetchType.EAGER) // Define que a coleção será carregada imediatamente com o usuário
    @CollectionTable(name = "tab_user_roles", joinColumns = @JoinColumn(name = "user_id")) // Define a tabela para os roles do usuário
    @Column(name = "role_id") // Define a coluna onde os roles serão armazenados
    private List<String> roles = new ArrayList<>(); // Lista de roles do usuário

    public User(){

    }
    public User(String username){
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }
}