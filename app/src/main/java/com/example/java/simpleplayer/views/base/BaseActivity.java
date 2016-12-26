package com.example.java.simpleplayer.views.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.views.CopyPasteHelper;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void addFragment(Fragment fragment) {
        fragmentTransaction(fragmentTransaction -> {
            fragmentTransaction.add(R.id.content_menu, fragment);
            fragmentTransaction.addToBackStack(null);
        });
    }

    protected void replaceFragment(Fragment fragment) {
        fragmentTransaction(fragmentTransaction -> {
            fragmentTransaction.replace(R.id.content_menu, fragment);
        });
    }

    protected void showMessage(CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void fragmentTransaction(CopyPasteHelper<FragmentTransaction> helper) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        helper.block(transaction);
        transaction.commit();
    }
}
