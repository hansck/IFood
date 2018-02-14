package com.tmpb.ifood.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hans CK on 10/31/2017.
 */
public class VerificationResponse {

	@SerializedName("sms-code")
	@Expose
	private String code;

	public VerificationResponse() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
