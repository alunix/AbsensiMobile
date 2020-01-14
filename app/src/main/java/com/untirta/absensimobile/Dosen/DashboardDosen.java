package com.untirta.absensimobile.Dosen;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.untirta.absensimobile.Mahasiswa.BerandaMahasiswa;
import com.untirta.absensimobile.Mahasiswa.HistoryMahasiswa;
import com.untirta.absensimobile.Mahasiswa.ProfileMahasiswa;
import com.untirta.absensimobile.R;

public class DashboardDosen extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_dosen);

        bottomNavigationView = findViewById(R.id.bottomnavigasiDosen);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.iconberanda_dosen:
                        fragment = new BerandaDosen();
                        changeFragment(fragment);
                        break;
                    case R.id.iconprofile_dosen:
                        fragment = new ProfileDosen();
                        changeFragment(fragment);
                        break;
                }
                return true;
            }
        });

        changeFragment(new BerandaDosen());

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutdosen, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }
}
