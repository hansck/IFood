package com.tmpb.ifood.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tmpb.ifood.BuildConfig;
import com.tmpb.ifood.model.response.VerificationResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hans CK on 6/23/2016.
 */
public class APIRequest {

	private static APIRequest instance = new APIRequest();
	private CacheInterceptor interceptor = new CacheInterceptor();

	private OkHttpClient cacheClient = new OkHttpClient.Builder().
		addNetworkInterceptor(interceptor).connectTimeout(10, TimeUnit.SECONDS).build();

	private Gson gson = new GsonBuilder()
		.excludeFieldsWithoutExposeAnnotation()
		.create();

	private APIService service = null;

	public APIRequest() {
		Retrofit retrofit = new Retrofit.Builder()
			.client(cacheClient)
			.baseUrl(BuildConfig.URL_API)
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build();

		service = retrofit.create(APIService.class);
	}

	public static APIRequest getInstance() {
		return instance;
	}

	public Gson getGson() {
		return gson;
	}

	//region GET Request
	//EP-USER-09: Get User's Condo Endpoint
	public void verifyPhoneNumber(final Map<String, String> params, final CallbackResponse<VerificationResponse> callbackResponse,
								  final CallbackError callbackError) {
		service.verifyPhoneNumber(params).enqueue(new Callback<VerificationResponse>() {
			@Override
			public void onResponse(Call<VerificationResponse> call, Response<VerificationResponse> response) {
				callbackResponse.setResponse(response);
				callbackResponse.setCall(call);
				callbackResponse.run();
			}

			@Override
			public void onFailure(Call<VerificationResponse> call, Throwable t) {
				callbackError.setThrowable(t);
				callbackError.run();
			}
		});
	}
}