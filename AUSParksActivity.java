package icn.proludic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static android.view.View.GONE;

/**
 * Author: Tom Linford
 * Date: 18/05/2018
 * Package: icn.proludic
 * Project Name: proludic
 */

public class AUSParksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aus_parks);
        setCustomToolbar();
    }

    public void setCustomToolbar() {
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setElevation(0);
        ab.setDisplayShowCustomEnabled(true);
        ab.setCustomView(R.layout.dashboard_toolbar);
        ab.getCustomView().findViewById(R.id.logo).setVisibility(GONE);
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
