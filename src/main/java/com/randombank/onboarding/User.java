package com.randombank.onboarding;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "app_user")
public final class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, columnDefinition = "UUID DEFAULT RANDOM_UUID() PRIMARY KEY")
    private UUID id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(
                            name = "country", column = @Column(
                            nullable = false, length = 2, columnDefinition = "CHAR(2) NOT NULL"
                    )
                    ),
                    @AttributeOverride(
                            name = "postalCode", column = @Column(nullable = false, length = 7)
                    ),
                    @AttributeOverride(
                            name = "streetAddress", column = @Column(nullable = false)
                    )
            }
    )
    Address address;
    @Column(nullable = false)
    private String dateOfBirth;
    @Column(nullable = false, length = 34)
    private String iban;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Column(nullable = false)
    private long accountBalanceCents;

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

    public void setCountry(String country) {
        this.address = new Address(country, this.address.postalCode(), this.address.streetAddress());
    }

    public void setPostalCode(String postalCode) {
        this.address = new Address(this.address.country(), postalCode, this.address.streetAddress());
    }

    public void setStreetAddress(String streetAddress) {
        this.address = new Address(this.address.country(), this.address.postalCode(), streetAddress);
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

    public void setAccountBalanceCents(long accountBalanceCents) {
        this.accountBalanceCents = accountBalanceCents;
    }

    public User() {
    }

    public User(
            String username,
            String password,
            String name,
            Address address,
            String dateOfBirth,
            String iban,
            AccountType accountType,
            long accountBalanceCents
    ) {
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

    public String country() {
        return address.country();
    }

    public String postalCode() {
        return address.postalCode();
    }

    public String streetAddress() {
        return address.streetAddress();
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

    public long accountBalanceCents() {
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
        return "User[id=%s, username=%s, password=%s, name=%s, address=%s, dateOfBirth=%s, iban=%s, accountType=%s, accountBalanceCents=%d]".formatted(id,
                username,
                password,
                name,
                address,
                dateOfBirth,
                iban,
                accountType,
                accountBalanceCents
        );
    }
}
