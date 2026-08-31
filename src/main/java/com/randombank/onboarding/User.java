package com.randombank.onboarding;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "app_user")
public final class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String dateOfBirth;
    @Column(nullable = false)
    private String iban;
    @Column(nullable = false)
    private AccountType accountType;
    @Column(nullable = false)
    private int accountBalanceCents;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setAccountBalanceCents(int accountBalanceCents) {
        this.accountBalanceCents = accountBalanceCents;
    }

    public User() {
    }

    public User(
            UUID id,
            String username,
            String password,
            String name,
            String address,
            String dateOfBirth,
            String iban,
            AccountType accountType,
            int accountBalanceCents
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.iban = iban;
        this.accountType = accountType;
        this.accountBalanceCents = accountBalanceCents;
    }

    public UUID id() {
        return id;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public String name() {
        return name;
    }

    public String address() {
        return address;
    }

    public String dateOfBirth() {
        return dateOfBirth;
    }

    public String iban() {
        return iban;
    }

    public AccountType accountType() {
        return accountType;
    }

    public int accountBalanceCents() {
        return accountBalanceCents;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (User) obj;
        return Objects.equals(this.id, that.id) && Objects.equals(
                this.username,
                that.username
        ) && Objects.equals(this.password, that.password) && Objects.equals(
                this.name,
                that.name
        ) && Objects.equals(this.address, that.address) && Objects.equals(
                this.dateOfBirth,
                that.dateOfBirth
        ) && Objects.equals(this.iban, that.iban) && Objects.equals(
                this.accountType,
                that.accountType
        ) && this.accountBalanceCents == that.accountBalanceCents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, name, address, dateOfBirth, iban, accountType, accountBalanceCents);
    }

    @Override
    public String toString() {
        return "User[" + "id=" + id + ", " + "username=" + username + ", " + "password=" + password + ", " + "name=" + name + ", " + "address=" + address + ", " + "dateOfBirth=" + dateOfBirth + ", " + "iban=" + iban + ", " + "accountType=" + accountType + ", " + "accountBalanceCents=" + accountBalanceCents + ']';
    }

}