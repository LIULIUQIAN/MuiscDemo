package com.example.muiscdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.muiscdemo.MainActivity;
import com.example.muiscdemo.R;
import com.example.muiscdemo.api.Api;
import com.example.muiscdemo.domain.Session;
import com.example.muiscdemo.domain.User;
import com.example.muiscdemo.domain.event.LoginSuccessEvent;
import com.example.muiscdemo.domain.response.DetailResponse;
import com.example.muiscdemo.reactivex.HttpListener;
import com.example.muiscdemo.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPhoneActivity extends BaseTitleActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
    }

    @OnClick(R.id.bt_register)
    public void onRegister() {

        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        if (StringUtils.isBlank(phone)) {
            ToastUtil.showSortToast(this, R.string.hint_phone);
            return;
        }
        if (StringUtils.isBlank(password)) {
            ToastUtil.showSortToast(this, R.string.hint_password);
            return;
        }

        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        user.setType(User.TYPE_PHONE);

        Api.getInstance().login(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<Session>>(getActivity()) {
                    @Override
                    public void onSucceeded(DetailResponse<Session> data) {
                        super.onSucceeded(data);
                        Session sessionMdoe = data.getData();
                        sp.setToken(sessionMdoe.getToken());
                        sp.setUserId(sessionMdoe.getId());
                        sp.setIMToken(sessionMdoe.getIm_token());

                        startActivityAfterFinishThis(MainActivity.class);

                        EventBus.getDefault().post(new LoginSuccessEvent());

                    }
                });

    }
}
