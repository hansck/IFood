package com.tmpb.ifood.api;

import com.tmpb.ifood.model.response.VerificationResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface APIService {

	//EP-USER-09
	//User Condo Endpoint
	@GET("sms-verification")
	Call<VerificationResponse> verifyPhoneNumber(@QueryMap(encoded = true) Map<String, String> params);
}
