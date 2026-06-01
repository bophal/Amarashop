package com.bookshop.domain;

import jakarta.persistence.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.*;

@Entity
public class BillingAddress {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String BillingAddressName;
	private String BillingAddressStreet1;
	private String BillingAddressStreet2;
	private String BillingAddressCity;
	private String BillingAddressState;
	private String BillingAddressCountry;
	private String BillingAddressZipcode;

    private String street;
    private String city;
    private String zip;
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	
	
	@OneToOne
	private Order order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBillingAddressName() {
		return BillingAddressName;
	}

	public void setBillingAddressName(String billingAddressName) {
		BillingAddressName = billingAddressName;
	}

	public String getBillingAddressStreet1() {
		return BillingAddressStreet1;
	}

	public void setBillingAddressStreet1(String billingAddressStreet1) {
		BillingAddressStreet1 = billingAddressStreet1;
	}

	public String getBillingAddressStreet2() {
		return BillingAddressStreet2;
	}

	public void setBillingAddressStreet2(String billingAddressStreet2) {
		BillingAddressStreet2 = billingAddressStreet2;
	}

	public String getBillingAddressCity() {
		return BillingAddressCity;
	}

	public void setBillingAddressCity(String billingAddressCity) {
		BillingAddressCity = billingAddressCity;
	}

	public String getBillingAddressState() {
		return BillingAddressState;
	}

	public void setBillingAddressState(String billingAddressState) {
		BillingAddressState = billingAddressState;
	}

	public String getBillingAddressCountry() {
		return BillingAddressCountry;
	}

	public void setBillingAddressCountry(String billingAddressCountry) {
		BillingAddressCountry = billingAddressCountry;
	}

	public String getBillingAddressZipcode() {
		return BillingAddressZipcode;
	}

	public void setBillingAddressZipcode(String billingAddressZipcode) {
		BillingAddressZipcode = billingAddressZipcode;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
