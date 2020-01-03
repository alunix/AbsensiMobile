package com.untirta.absensimobile.QRScaner;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.untirta.absensimobile.Mahasiswa.BerandaMahasiswa;
import com.untirta.absensimobile.Mahasiswa.DashboardMahasiswa;
import com.untirta.absensimobile.Mahasiswa.SuccesDialog;
import com.untirta.absensimobile.R;


public class AbsenMahasiswa extends DialogFragment {

    ImageView imageView;
    CodeScannerView scannerView;
    CodeScanner codeScanner;
    String namamk;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.absensi_mahasiswa,container,false);


        Bundle args = getArguments();
        namamk = args.getString("namamk");

        imageView = view.findViewById(R.id.bgcontent);
        scannerView = view.findViewById(R.id.scanerview);

        imageView.bringToFront();

        codeScanner = new CodeScanner(getContext(),scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (result.getText().equals(String.valueOf(namamk))){
                            Bundle arg = new Bundle();
                            arg.putString("namamk",namamk);
                            DialogFragment fragment = new SuccesDialog();
                            fragment.setArguments(arg);
                            fragment.show(getFragmentManager(),"SuccesDialog");
                        } else {
                            Toast.makeText(getContext(),"QR Code tidak cocok",Toast.LENGTH_LONG).show();
                            codeScanner.startPreview();
                        }
                    }
                });
            }
        });

        checkpermisi();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkpermisi();
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private void checkpermisi() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        codeScanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(),"Aktifkan Izin Camera untuk melakukan absen",Toast.LENGTH_LONG).show();
                        dismiss();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

}
