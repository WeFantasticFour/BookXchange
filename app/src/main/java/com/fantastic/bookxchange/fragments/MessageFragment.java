package com.fantastic.bookxchange.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.User;


public class MessageFragment extends BottomSheetDialogFragment {


    private static final int TOTAL_CHARS = 50;

    private EditText etMessage;
    private Button btnGoToSendMessage;
    private Button btnCancel;
    private TextView tvCharsRemaining;

    User user;
    private MessageListener mListener;

    public interface MessageListener{
        void onAddMessage(String s);
    }

    public MessageFragment() {
        //Empty constructor is required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_message, container);
    }

    public static MessageFragment newInstance() {
        MessageFragment messageFragment = new MessageFragment();

        return messageFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        etMessage = view.findViewById(R.id.etMessage);
        btnGoToSendMessage = view.findViewById(R.id.btnGoToSendMessage);
        tvCharsRemaining = view.findViewById(R.id.tvCharactersRemaining);
        btnCancel = view.findViewById(R.id.btnCancel);

        getDialog().setTitle("Message");
        // Hide soft keyboard automatically and request focus to field
        etMessage.requestFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

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

        btnGoToSendMessage.setOnClickListener(v -> {
            if(etMessage.getText().toString().replaceAll("\\s+","").length() == 0) {
                Toast.makeText(getContext(), "Empty message", Toast.LENGTH_SHORT);
            }else {
                addMessage(etMessage.getText().toString());
                dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());

        setCharsRemaining(TOTAL_CHARS);

    }

    private void setCharsRemaining(int totalChars) {
        Resources res = getContext().getResources();
        tvCharsRemaining.setText(String.format(res.getString(R.string.number), totalChars));
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessageListener) {
            mListener = (MessageListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void addMessage(String s) {
        if (mListener != null) {
            mListener.onAddMessage(s);
        }
    }
}
