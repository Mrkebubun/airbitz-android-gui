package com.airbitz.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbitz.R;
import com.airbitz.activities.NavigationActivity;

/**
 * Created on 2/24/14.
 */
public class WalletPasswordFragment extends Fragment {

    private EditText mPasswordEdittext;
    private ImageView mValidPasswordImageView;

    private ImageButton mBackButton;
    private ImageButton mHelpButton;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parentViewGroup = (ViewGroup) mView.getParent();
        if (null != parentViewGroup) {
            parentViewGroup.removeView(mView);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView != null)
            return mView;
        mView = inflater.inflate(R.layout.fragment_wallet_password, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mPasswordEdittext = (EditText) mView.findViewById(R.id.wallet_password_edittext_password);
        mValidPasswordImageView = (ImageView) mView.findViewById(R.id.imageview_valid_password);

        mBackButton = (ImageButton) mView.findViewById(R.id.layout_airbitz_header_button_back);
        mHelpButton = (ImageButton) mView.findViewById(R.id.layout_airbitz_header_button_help);


        TextView titleTextView = (TextView) mView.findViewById(R.id.fragment_category_textview_title);
        titleTextView.setTypeface(NavigationActivity.montserratBoldTypeFace);

        mPasswordEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    if (((EditText) view).getText().length() != 0) {

                    }
                }
            }
        });

        mPasswordEdittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                int keyAction = keyEvent.getAction();
                String test = "";
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);


                if (keyAction == KeyEvent.ACTION_UP) {
                    switch (keyCode) {
                        case KeyEvent.FLAG_EDITOR_ACTION:
                            imm.hideSoftInputFromWindow(mPasswordEdittext.getWindowToken(), 0);
                            mValidPasswordImageView.setVisibility(View.VISIBLE);

                            if (mPasswordEdittext.getText().toString().equals("Password")) {
                                mValidPasswordImageView.setImageResource(R.drawable.ico_approved);
                            } else {
                                mValidPasswordImageView.setImageResource(R.drawable.ico_not_approved);
                            }
                            return true;
                        case KeyEvent.KEYCODE_ENTER:
                            imm.hideSoftInputFromWindow(mPasswordEdittext.getWindowToken(), 0);
                            mValidPasswordImageView.setVisibility(View.VISIBLE);

                            if (mPasswordEdittext.getText().toString().equals("Password")) {
                                mValidPasswordImageView.setImageResource(R.drawable.ico_approved);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
//                                        mIntent = new Intent(WalletPasswordFragment.this, TransactionActivity.class);
//                                        startActivity(mIntent);
//                                        finish();
                                    }
                                }, 2000);
                            } else {
                                mValidPasswordImageView.setImageResource(R.drawable.ico_not_approved);
                            }

                            return true;
                        default:
                            return false;
                    }
                }

                return false;
            }

        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return mView;
    }

}
