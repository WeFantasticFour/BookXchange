package com.fantastic.bookxchange.activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.fantastic.bookxchange.R;

/**
 * Created by dgohil on 10/12/17.
 */

public class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected ActionBar actionBar;

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        toolbar = findViewById(R.id.app_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
        }
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    public void enableToolbarBackButton() {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void showToolbarBackButton() {
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public void setTitle(@StringRes int title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void startActivity(Class klass) {
        startActivity(new Intent(this, klass));
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void toast(@StringRes int resId) {
        toast(getString(resId));
    }

    public void snakebar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    public void snakebar(View view, @StringRes int resId) {
        snakebar(view, getString(resId));
    }

    protected void matchStatusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (isLollipop()) {
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
        }
    }


    public boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public String getText(EditText text) {
        if (text == null)
            return null;

        return text.getText().toString().trim();
    }

    protected boolean validate(EditText... editTexts) {
        int inValidCount = 0;
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(getText(editText))) {
                editText.setError("Required.");
                inValidCount++;
            } else {
                editText.setError(null);
            }
        }
        return inValidCount == 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
