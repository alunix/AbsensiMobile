package com.untirta.absensimobile.Mahasiswa;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.tapadoo.alerter.Alerter;
import com.untirta.absensimobile.R;

public class BerandaMahasiswa extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beranda_mahasiswa,container,false);


        checkEmailVerify();

        return view;
    }

    private void checkEmailVerify() {
        if (firebaseAuth.getCurrentUser().isEmailVerified() == false){
            Alerter.create(getActivity())
                    .setTitle("Verifikasi E-mail").setTitleAppearance(R.style.CostumTitleAlert)
                    .setText("Periksa e-mail masuk kamu untuk mendapatkan link verifikasi").setTextAppearance(R.style.CostumTextAlert)
                    .enableSwipeToDismiss().setProgressColorRes(R.color.colormenu).enableProgress(true)
                    .setBackgroundColorRes(R.color.colorWhite).setDuration(8000)
                    .setIcon(getResources().getDrawable(R.drawable.icon_alert)).setIconColorFilter(getResources().getColor(R.color.colorText))
                    .showIcon(true)
                    .show();
        } else {
            Alerter.hide();
        }

    }
/*
    @Override
    public void onResume() {
        super.onResume();
        String pertamakali = "SOME_KEY";
        Boolean first = getActivity().getPreferences(Context.MODE_PRIVATE).getBoolean(pertamakali,true);
        if (first){
            checkEmailVerify();
            getActivity().getPreferences(Context.MODE_PRIVATE).edit().putBoolean(pertamakali,false).apply();
        }
    } */
}
