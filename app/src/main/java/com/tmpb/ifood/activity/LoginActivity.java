package com.tmpb.ifood.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tmpb.ifood.R;
import com.tmpb.ifood.util.Common;
import com.tmpb.ifood.util.manager.UserManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

@EActivity(R.layout.activity_login)
@WindowFeature(Window.FEATURE_NO_TITLE)
public class LoginActivity extends AppCompatActivity {

	@ViewById
	ProgressBar progressBar;
	@ViewById
	Button btnSignIn;

	private static final int RC_SIGN_IN = 9001;
	private FirebaseAuth.AuthStateListener listener;
	private boolean onCreate = true;

	@AfterViews
	void initLayout() {
		listener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				UserManager.getInstance().setFirebaseUser(firebaseAuth.getCurrentUser());
				if (UserManager.getInstance().getFirebaseUser() != null) {
					goToHome();
				} else {
					if (onCreate) {
						onCreate = false;
					} else {
						setLoading(false);
						Common.getInstance().showAlertToast(LoginActivity.this, getString(R.string.default_failed));
					}
				}
			}
		};
	}

	@Click(R.id.btnSignIn)
	void signIn() {
		setLoading(true);
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(UserManager.getInstance().getClient());
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				GoogleSignInAccount account = result.getSignInAccount();
				firebaseAuthWithGoogle(account);
				UserManager.getInstance().setKeyStore(account);
			} else {
				setLoading(false);
				Common.getInstance().showAlertToast(this, getString(R.string.default_failed));
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		UserManager.getInstance().getAuth().addAuthStateListener(listener);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (listener != null) {
			UserManager.getInstance().getAuth().removeAuthStateListener(listener);
		}
	}

	//region Private Methods
	private void setLoading(boolean loading) {
		progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
		btnSignIn.setVisibility(loading ? View.GONE : View.VISIBLE);
	}

	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		UserManager.getInstance().getAuth().signInWithCredential(credential)
			.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					if (!task.isSuccessful()) {
						Common.getInstance().showAlertToast(LoginActivity.this, getString(R.string.default_failed));
					}
				}
			});
	}

	private void goToHome() {
		Intent intent = new Intent(this, HomeActivity_.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
	}

	private void goToVerification() {
		Intent intent = new Intent(this, VerificationActivity_.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
	}
	//endregion

	//region Firebase Call
//	public void checkUserStatus() {
//		if (UserManager.getInstance().getAccount() != null) {
//			String email = UserManager.getInstance().getAccount().getEmail();
//			final DatabaseReference ref = FirebaseDB.getInstance().getDbReference(Constants.User.ROLE);
//			ref.orderByChild(Constants.User.EMAIL).equalTo(email).addValueEventListener(new ValueEventListener() {
//				@Override
//				public void onDataChange(DataSnapshot dataSnapshot) {
//					boolean isAdmin = false;
//					for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//						isAdmin = true;
//					}
//					UserManager.getInstance().setUserRole(isAdmin);
//					ref.removeEventListener(this);
//					goToHome();
//				}
//
//				@Override
//				public void onCancelled(DatabaseError error) {
//					setLoading(false);
//					ref.removeEventListener(this);
//				}
//			});
//		}
//	}

//	public void checkUserData() {
//		if (UserManager.getInstance().getAccount() != null) {
//			String email = UserManager.getInstance().getAccount().getEmail();
//			final DatabaseReference ref = FirebaseDB.getInstance().getDbReference(Constants.User.USER);
//			ref.orderByChild(Constants.User.EMAIL).equalTo(email).addValueEventListener(new ValueEventListener() {
//				@Override
//				public void onDataChange(DataSnapshot dataSnapshot) {
//					User user = null;
//					for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//						user = postSnapshot.getValue(User.class);
//						user.setKey(postSnapshot.getKey());
//					}
//					setLoading(false);
//					if (user != null) {
//						UserManager.getInstance().setUser(user);
//						if (user.isVerified()) {
//							goToHome();
//						} else {
//							goToVerification();
//						}
//					} else {
//						goToFirstLogin();
//					}
//					ref.removeEventListener(this);
//				}
//
//				@Override
//				public void onCancelled(DatabaseError error) {
//					setLoading(false);
//					ref.removeEventListener(this);
//				}
//			});
//		}
//	}
	//endregion
}

