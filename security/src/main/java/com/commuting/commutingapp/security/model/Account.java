package com.commuting.commutingapp.security.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "account_id", updatable = false, nullable = false)
    private String id;

    private String email;

    boolean emailVerified;

    private String phoneNumber;

    boolean phoneNumberVerified;

    private String firstName;

    private String lastName;

    private String emailVerificationKey;

    private String phoneNumberVerificationKey;

    private Instant emailVerificationDate = null;

    private Instant phoneNumberVerificationDate = null;

    private String fcmToken;

    @ElementCollection
    @CollectionTable(name="Authorities", joinColumns=@JoinColumn(name="user_id"))
    private List<String> authorities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public void setPhoneNumberVerified(boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailVerificationKey() {
        return emailVerificationKey;
    }

    public void setEmailVerificationKey(String emailVerificationKey) {
        this.emailVerificationKey = emailVerificationKey;
    }

    public String getPhoneNumberVerificationKey() {
        return phoneNumberVerificationKey;
    }

    public void setPhoneNumberVerificationKey(String phoneNumberVerificationKey) {
        this.phoneNumberVerificationKey = phoneNumberVerificationKey;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Instant getEmailVerificationDate() {
        return emailVerificationDate;
    }

    public void setEmailVerificationDate(Instant emailVerificationDate) {
        this.emailVerificationDate = emailVerificationDate;
    }

    public Instant getPhoneNumberVerificationDate() {
        return phoneNumberVerificationDate;
    }

    public void setPhoneNumberVerificationDate(Instant phoneNumberVerificationDate) {
        this.phoneNumberVerificationDate = phoneNumberVerificationDate;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}
