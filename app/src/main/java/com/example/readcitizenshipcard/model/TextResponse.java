package com.example.readcitizenshipcard.model;

import com.google.gson.annotations.SerializedName;

public class TextResponse {

	@SerializedName("dob_year")
	private String dobYear;

	@SerializedName("full_name")
	private String fullName;

	@SerializedName("p_address_ward_no")
	private String pAddressWardNo;

	@SerializedName("sex")
	private String sex;

	@SerializedName("citizen_no")
	private String citizenNo;

	@SerializedName("p_address_area")
	private String pAddressArea;

	@SerializedName("dob_month")
	private String dobMonth;

	@SerializedName("p_address_district")
	private String pAddressDistrict;

	@SerializedName("birth_place_area")
	private String birthPlaceArea;

	@SerializedName("birth_place_district")
	private String birthPlaceDistrict;

	@SerializedName("birth_place_ward_no")
	private String birthPlaceWardNo;

	@SerializedName("dob_day")
	private String dobDay;

	public String getDobYear(){
		return dobYear;
	}

	public String getFullName(){
		return fullName;
	}

	public String getPAddressWardNo(){
		return pAddressWardNo;
	}

	public String getSex(){
		return sex;
	}

	public String getCitizenNo(){
		return citizenNo;
	}

	public String getPAddressArea(){
		return pAddressArea;
	}

	public String getDobMonth(){
		return dobMonth;
	}

	public String getPAddressDistrict(){
		return pAddressDistrict;
	}

	public String getBirthPlaceArea(){
		return birthPlaceArea;
	}

	public String getBirthPlaceDistrict(){
		return birthPlaceDistrict;
	}

	public String getBirthPlaceWardNo(){
		return birthPlaceWardNo;
	}

	public String getDobDay(){
		return dobDay;
	}
}