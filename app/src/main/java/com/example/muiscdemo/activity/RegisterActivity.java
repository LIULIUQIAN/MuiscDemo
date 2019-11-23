package com.example.muiscdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.muiscdemo.R;
import com.example.muiscdemo.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseTitleActivity {

    @BindView(R.id.et_nickname)
    EditText etNickname;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @OnClick(R.id.bt_register)
    public void onRegister(){
        String nickname = etNickname.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (StringUtils.isBlank(nickname)){
            ToastUtil.showSortToast(this,R.string.enter_nickname);
            return;
        }
        if (StringUtils.isBlank(phone)){
            ToastUtil.showSortToast(this,R.string.hint_phone);
            return;
        }
        if (StringUtils.isBlank(password)){
            ToastUtil.showSortToast(this,R.string.hint_password);
            return;
        }

    }
}
