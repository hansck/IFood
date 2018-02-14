package com.tmpb.ifood.api;

import com.tmpb.ifood.util.ConnectivityUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Hans CK on 6/30/2016.
 */
public class CacheInterceptor implements Interceptor {
	private String body = "";

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		if (request.method().equals("GET")) {
			if (ConnectivityUtil.getInstance().isNetworkConnected()) {
				request = request.newBuilder().
					removeHeader("pragma").
					removeHeader("Cache-Control").
					header("Cache-Control", "only-if-cached").build();
			} else {
				request = request.newBuilder().
					removeHeader("pragma").
					removeHeader("Cache-Control").
					header("Cache-control", "max-stale=2419200").build();
			}
		}
		Response response = chain.proceed(request);
		body = response.body().string();
		return response.newBuilder().body(ResponseBody.create(response.body().contentType(), body)).build();
	}

	public String getBody() {
		return body;
	}
}
