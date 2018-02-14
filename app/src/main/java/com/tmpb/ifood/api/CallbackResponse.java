package com.tmpb.ifood.api;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Hans CK on 6/23/2016.
 */
public abstract class CallbackResponse<Type> implements Runnable {

	private Call<Type> call;
	private Response<Type> response;

	public void setResponse(Response<Type> _response) {
		response = _response;
	}

	public Response<Type> getResponse() {
		return response;
	}

	public Call<Type> getCall() {
		return call;
	}

	public void setCall(Call<Type> _call) {
		call = _call;
	}
}
