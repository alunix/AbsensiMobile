package com.untirta.absensimobile.Mahasiswa;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.untirta.absensimobile.R;

public class DashboardMahasiswa extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_mahasiswa);

        bottomNavigationView = findViewById(R.id.bottomnavigasiMahasiswa);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.iconberanda_mahasiswa:
                        fragment = new BerandaMahasiswa();
                        changeFragment(fragment);
                        break;
                    case R.id.iconhistory_mahasiswa:
                        fragment = new HistoryMahasiswa();
                        changeFragment(fragment);
                        break;
                    case R.id.iconprofile_mahasiswa:
                        fragment = new ProfileMahasiswa();
                        changeFragment(fragment);
                        break;
                }
                return true;
            }
        });

        changeFragment(new BerandaMahasiswa());

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutMahasiswa, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }
}
