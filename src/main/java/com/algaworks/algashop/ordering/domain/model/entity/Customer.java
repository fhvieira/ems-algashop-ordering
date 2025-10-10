package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exceptions.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import lombok.Builder;

import java.time.Instant;
import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.*;

public class Customer implements AggregateRoot<CustomerId>{
    private CustomerId id;
    private FullName fullName;
    private BirthDate birthDate;
    private Email email;
    private Phone phone;
    private Document document;
    private Boolean promotionNotificationsAllowed;
    private Boolean archived;
    private Instant registeredAt;
    private Instant archivedAt;
    private LoyaltyPoints loyaltyPoints;
    private Address address;

    @Builder(builderClassName = "BrandNewCustomerBuilder", builderMethodName = "brandNewBuilder")
    private static Customer brandNew(FullName fullName, BirthDate birthDate, Email email, Phone phone, Document document,
                                    Boolean promotionNotificationsAllowed, Address address) {
        return new Customer(
                new CustomerId(),
                fullName,
                birthDate,
                email,
                phone,
                document,
                promotionNotificationsAllowed,
                false,
                Instant.now(),
                null,
                LoyaltyPoints.ZERO,
                address);
    }

    @Builder(builderClassName = "ExistingCustomerBuilder", builderMethodName = "existingBuilder")
    private Customer(CustomerId id, FullName fullName, BirthDate birthDate, Email email, Phone phone, Document document,
                    Boolean promotionNotificationsAllowed, Boolean archived, Instant registeredAt,
                    Instant archivedAt, LoyaltyPoints loyaltyPoints, Address address) {
        this.setId(id);
        this.setFullName(fullName);
        this.setBirthDate(birthDate);
        this.setEmail(email);
        this.setPhone(phone);
        this.setDocument(document);
        this.setPromotionNotificationsAllowed(promotionNotificationsAllowed);
        this.setArchived(archived);
        this.setRegisteredAt(registeredAt);
        this.setArchivedAt(archivedAt);
        this.setLoyaltyPoints(loyaltyPoints);
        this.setAddress(address);
    }

    public void addLoyaltyPoints(LoyaltyPoints points) {
        isChangeable();
        this.setLoyaltyPoints(this.loyaltyPoints().add(points));
    }

    public void archive() {
        isChangeable();
        this.setFullName(new FullName("anonymous", "anonymous"));
        this.setEmail(new Email("anonymous@email.com"));
        this.setPhone(new Phone("000-000-0000"));
        this.setDocument(new Document("000-00-0000"));
        this.setBirthDate(null);
        this.setArchived(true);
        this.setArchivedAt(Instant.now());
        this.setPromotionNotificationsAllowed(false);
        this.setAddress(this.address().toBuilder()
                        .additionalInfo("anonymous")
                        .build());
    }

    public void enablePromotionNotification() {
        isChangeable();
        this.setPromotionNotificationsAllowed(true);
    }

    public void disablePromotionNotification() {
        isChangeable();
        this.setPromotionNotificationsAllowed(false);
    }

    public void changeName(FullName fullName) {
        isChangeable();
        this.setFullName(fullName);
    }

    public void changeEmail(Email email) {
        isChangeable();
        this.setEmail(email);
    }

    public void changePhone(Phone phone) {
        isChangeable();
        this.setPhone(phone);
    }

    public void changeAddress(Address address) {
        isChangeable();
        this.setAddress(address);
    }

    public CustomerId id() {
        return id;
    }

    public FullName fullName() {
        return fullName;
    }

    public BirthDate birthDate() {
        return birthDate;
    }

    public Email email() {
        return email;
    }

    public Phone phone() {
        return phone;
    }

    public Document document() {
        return document;
    }

    public Boolean isPromotionNotificationsAllowed() {
        return promotionNotificationsAllowed;
    }

    public Boolean isArchived() {
        return archived;
    }

    public Instant registeredAt() {
        return registeredAt;
    }

    public Instant archivedAt() {
        return archivedAt;
    }

    public LoyaltyPoints loyaltyPoints() {
        return loyaltyPoints;
    }

    public Address address() {
        return address;
    }

    private void setId(CustomerId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setFullName(FullName fullName) {
        Objects.requireNonNull(fullName, CUSTOMER_FULLANME_INVALID);
        this.fullName = fullName;
    }

    private void setBirthDate(BirthDate birthDate) {
        if (birthDate == null) {
            this.birthDate = birthDate;
            return;
        }
        this.birthDate = birthDate;
    }

    private void setEmail(Email email) {
        this.email = email;
    }

    private void setPhone(Phone phone) {
        this.phone = phone;
    }

    private void setDocument(Document document) {
        this.document = document;
    }

    private void setPromotionNotificationsAllowed(Boolean promotionNotificationsAllowed) {
        Objects.requireNonNull(promotionNotificationsAllowed);
        this.promotionNotificationsAllowed = promotionNotificationsAllowed;
    }

    private void setArchived(Boolean archived) {
        Objects.requireNonNull(archived);
        this.archived = archived;
    }

    private void setRegisteredAt(Instant registeredAt) {
        Objects.requireNonNull(registeredAt);
        this.registeredAt = registeredAt;
    }

    private void setArchivedAt(Instant archivedAt) {
        this.archivedAt = archivedAt;
    }

    private void setLoyaltyPoints(LoyaltyPoints loyaltyPoints) {
        Objects.requireNonNull(loyaltyPoints);
        this.loyaltyPoints = loyaltyPoints;
    }

    private void setAddress(Address address) {
        Objects.requireNonNull(address);
        this.address = address;
    }

    private void isChangeable() {
        if (this.isArchived()) {
            throw new CustomerArchivedException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
