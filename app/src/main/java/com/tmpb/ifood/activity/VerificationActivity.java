package com.tmpb.ifood.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.tmpb.ifood.BuildConfig;
import com.tmpb.ifood.R;
import com.tmpb.ifood.api.APIRequest;
import com.tmpb.ifood.api.CallbackError;
import com.tmpb.ifood.api.CallbackResponse;
import com.tmpb.ifood.model.response.VerificationResponse;
import com.tmpb.ifood.util.Common;
import com.tmpb.ifood.util.ConnectivityUtil;
import com.tmpb.ifood.util.Constants;
import com.tmpb.ifood.util.FirebaseDB;
import com.tmpb.ifood.util.UserManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

@EActivity(R.layout.activity_verification)
public class VerificationActivity extends AppCompatActivity {

	private Date date;

	@ViewById
	EditText phoneNumber;
	@ViewById
	EditText verifyCode;
	@ViewById
	Button btnVerify;
	@ViewById
	ProgressBar progressBar;

	@AfterViews
	void initLayout() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		phoneNumber.setText(UserManager.getInstance().getUserPhone());
	}

	@Click(R.id.btnVerify)
	void onVerify() {
		String verification = verifyCode.getText().toString();
		if (!verification.isEmpty() && verification.equals(UserManager.getInstance().getVerificationCode())) {
			setLoading(true);
			setUserVerified();
		} else {

			Common.getInstance().showAlertToast(this, getString(R.string.verification_failed));
		}
	}

	@Click(R.id.btnResend)
	void onResend() {
		String phone = phoneNumber.getText().toString();
		UserManager.getInstance().setUserPhone(phone);
		verifyPhoneNumber(phone);
	}

	//region Private methods
	private void setLoading(boolean loading) {
		btnVerify.setVisibility(loading ? View.GONE : View.VISIBLE);
		progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
	}

	private void setUserVerified() {
		DatabaseReference ref = FirebaseDB.getInstance().getDbReference(Constants.User.USER);
		Map<String, Object> taskMap = new HashMap<>();
		taskMap.put("verified", true);
		ref.child(UserManager.getInstance().getUserKey()).updateChildren(taskMap);
		setLoading(false);
		goToHome();
	}

	private void goToHome() {
		Intent intent = new Intent(this, MainActivity_.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
	}
	//endregion

	//region API Call
	private void verifyPhoneNumber(String phone) {
		Map<String, String> params = new HashMap<>();
		params.put(Constants.APIKey.PASSWORD, BuildConfig.PASSWORD);
		params.put(Constants.APIKey.PHONE, phone);

		APIRequest.getInstance().verifyPhoneNumber(params, new CallbackResponse<VerificationResponse>() {
			@Override
			public void run() {
				setLoading(false);
				Response<VerificationResponse> response = getResponse();
				if (!response.isSuccessful()) {
					Common.getInstance().showAlertToast(VerificationActivity.this, getString(R.string.default_failed));
				} else {
					VerificationResponse verificationResponse = response.body();
					if (ConnectivityUtil.getInstance().isSuccess(response.code())) {
						UserManager.getInstance().setVerificationCode(verificationResponse.getCode());
						Common.getInstance().showAlertToast(VerificationActivity.this, getString(R.string.verification_resend_alert));
					} else {
						Common.getInstance().showAlertToast(VerificationActivity.this, getString(R.string.default_failed));
					}
				}
			}
		}, new CallbackError() {
			@Override
			public void run() {
				Common.getInstance().showAlertToast(VerificationActivity.this, getString(R.string.default_failed));
				setLoading(false);
			}
		});
	}
	//endregion
}
