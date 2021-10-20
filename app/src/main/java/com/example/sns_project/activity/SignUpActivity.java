package com.example.sns_project.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.sns_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.sns_project.Util.showToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        setToolbarTitle("회원가입");

        mAuth = FirebaseAuth.getInstance();

        signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpButton:
                signUp();
                break;
        }
    }

    private void signUp() {
        EditText edtEmail = findViewById(R.id.emailEditText);
        EditText edtPassword = findViewById(R.id.passwordEditText);
        EditText edtPasswordCheck = findViewById(R.id.passwordCheckEditText);

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String passwordCheck = edtPasswordCheck.getText().toString();

        if (email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if (password.equals(passwordCheck)) {
                final RelativeLayout loaderLayout = findViewById(R.id.loaderLyaout);
                loaderLayout.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loaderLayout.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    showToast(SignUpActivity.this, "회원가입에 성공하였습니다.");
                                    myStartActivity(MainActivity.class);
                                } else {
                                    if (task.getException() != null) {
                                        showToast(SignUpActivity.this, task.getException().toString());
                                    }
                                }
                            }
                        });
            } else {
                showToast(SignUpActivity.this, "비밀번호가 일치하지 않습니다.");
            }
        } else {
            showToast(SignUpActivity.this, "이메일 또는 비밀번호를 입력해 주세요.");
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
