package cm.aptoide.accountmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by trinkes on 4/29/16.
 */
public class SignUpActivity extends BaseActivity implements AptoideAccountManager.IRegisterUser {

	private Button signUpButton;
	private Toolbar mToolbar;
	private EditText password_box;
	private EditText emailBox;
	private Button hidePasswordButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		bindViews();
		setupToolbar();
		setupListeners();
	}

	@Override
	protected String getActivityTitle() {
		return "Sign up";
	}

	@Override
	int getLayoutId() {
		return R.layout.sign_up_activity_layout;
	}

	private void setupListeners() {
		setupShowHidePassButton();
		AptoideAccountManager.setupRegisterUser(this, signUpButton);
	}

	private void setupToolbar() {
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(true);
			getSupportActionBar().setTitle(getString(R.string.register));
		}
	}

	private void bindViews() {
		signUpButton = (Button) findViewById(R.id.submitCreateUser);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		emailBox = (EditText) findViewById(R.id.username);
		password_box = (EditText) findViewById(R.id.password);
		hidePasswordButton = (Button) findViewById(R.id.btn_show_hide_pass);
	}

	private void setupShowHidePassButton() {
		hidePasswordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final int cursorPosition = password_box.getSelectionStart();
				final boolean passwordShown = password_box.getTransformationMethod() == null;
				v.setBackgroundResource(passwordShown ? R.drawable.icon_closed_eye : R.drawable
						.icon_open_eye);
				password_box.setTransformationMethod(passwordShown ? new
						PasswordTransformationMethod() : null);
				password_box.setSelection(cursorPosition);
			}
		});
	}

	@Override
	public void onRegisterSuccess(Bundle data) {
		Toast.makeText(SignUpActivity.this, "User Registed", Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK, new Intent().putExtras(data));
		finish();
	}

	@Override
	public void onRegisterFail(@StringRes int reason) {
		Toast.makeText(SignUpActivity.this, reason, Toast.LENGTH_SHORT).show();
	}

	@Override
	public String getUserPassword() {
		return password_box == null ? "" : password_box.getText().toString();
	}

	@Override
	public String getUserEmail() {
		return emailBox == null ? "" : emailBox.getText().toString();
	}
}