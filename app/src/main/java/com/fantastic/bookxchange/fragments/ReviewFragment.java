package com.fantastic.bookxchange.fragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Review;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewFragment.ReviewDialogListener} interface
 * to handle interaction events.
 */
public class ReviewFragment extends DialogFragment {

    private ReviewDialogListener mListener;

    Button btnAddReview;
    RatingBar ratingBar;
    EditText etReview;

    public ReviewFragment() {
        // Required empty public constructor
    }

    public interface ReviewDialogListener {
        void onAddReview(Review review);
    }

    public static ReviewFragment newInstance(){
        ReviewFragment fragment = new ReviewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.fragment_review, container, false);
        btnAddReview = v.findViewById(R.id.btnAddReview);
        etReview = v.findViewById(R.id.etReview);
        ratingBar = v.findViewById(R.id.rbStars);

        btnAddReview.setOnClickListener(view -> {
            if(etReview.getText().length() == 0) {
                Toast.makeText(getContext(), "Write a review", Toast.LENGTH_SHORT).show();
            }else{
                Review review = new Review();
                review.setStars(ratingBar.getRating());
                review.setReview(etReview.getText().toString());
                mListener.onAddReview(review);
                dismiss();
            }
        });

        return v;
    }

    public void addReview(Review review) {
        if (mListener != null) {
            mListener.onAddReview(review);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ReviewDialogListener) {
            mListener = (ReviewDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}
