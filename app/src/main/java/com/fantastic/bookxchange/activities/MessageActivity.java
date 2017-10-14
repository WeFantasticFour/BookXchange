package com.fantastic.bookxchange.activities;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fantastic.bookxchange.R;

import static java.security.AccessController.getContext;

public class MessageActivity extends BaseActivity {

    private static final int TOTAL_CHARS = 50;

    private EditText etMessage;
    private Button btnGoToSendMessage;
    private Button btnCancel;
    private TextView tvCharsRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        etMessage = findViewById(R.id.etMessage);
        btnGoToSendMessage = findViewById(R.id.btnGoToSendMessage);
        tvCharsRemaining = findViewById(R.id.tvCharactersRemaining);
        btnCancel = findViewById(R.id.btnCancel);

        setupToolbar();

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharsRemaining.setText(String.valueOf(TOTAL_CHARS - s.length()) + " / " + TOTAL_CHARS);
                if(s.length()> 0 && s.length() <= TOTAL_CHARS){
                    btnGoToSendMessage.setEnabled(true);
                }
                else{
                    btnGoToSendMessage.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnGoToSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO SEND MESSAGE
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setCharsRemaining(TOTAL_CHARS);


    }

    private void setCharsRemaining(int totalChars) {

        tvCharsRemaining.setText(String.format(getString(R.string.number), totalChars));
    }

    private void setupToolbar() {
        enableToolbarBackButton();
        setTitle("Message");
    }


}
