package com.airbitz.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbitz.R;
import com.airbitz.activities.NavigationActivity;
import com.airbitz.api.CoreAPI;
import com.airbitz.models.HighlightOnPressButton;
import com.airbitz.models.Wallet;
import com.airbitz.utils.Common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class RequestQRCodeFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private ImageView mQRView;
    private ImageButton mBackButton;
    private ImageButton mHelpButton;
    private HighlightOnPressButton mCancelButton;
    private HighlightOnPressButton mSMSButton;
    private HighlightOnPressButton mEmailButton;
    private HighlightOnPressButton mCopyButton;
    private TextView mBitcoinAmount;
    private TextView mBitcoinAddress;

    private Bitmap mQRBitmap;
    private String mID;
    private String mAddress;
    private String mContentURL;
    private String mRequestURI;

    private List<String> mContactNames = new ArrayList<String>();
    private Map<String, String> mContactPhones = new LinkedHashMap<String, String>();


    private Bundle bundle;

    private Wallet mWallet;

    private CoreAPI mCoreAPI;
    private View mView;
    static final int PICK_CONTACT_SMS =1;
    static final int PICK_CONTACT_EMAIL=2;
    private enum SendType {SEND_EMAIL, SEND_SMS};
    private SendType mSendType = SendType.SEND_EMAIL;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        mCoreAPI = CoreAPI.getApi();
    }

    @Override
    public void onPause() {
        if(mContentURL!=null) { // delete temp file
            Log.d("WalletQRCodeFragment", "deleting temp file");
            getActivity().getContentResolver().delete(Uri.parse(mContentURL), null, null);
        }
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWallet = mCoreAPI.getWalletFromName(bundle.getString(Wallet.WALLET_NAME));

        mView = inflater.inflate(R.layout.fragment_wallet_qrcode, container, false);
        ((NavigationActivity)getActivity()).hideNavBar();

        mQRView = (ImageView) mView.findViewById(R.id.qr_code_view);

        mBitcoinAmount = (TextView) mView.findViewById(R.id.textview_bitcoin_amount);
        mBitcoinAmount.setText(bundle.getString(RequestFragment.BITCOIN_VALUE));

        mBitcoinAddress = (TextView) mView.findViewById(R.id.textview_address);

        mBackButton = (ImageButton) mView.findViewById(R.id.button_back);
        mHelpButton = (ImageButton) mView.findViewById(R.id.button_help);
        mCopyButton = (HighlightOnPressButton) mView.findViewById(R.id.fragment_qrcode_copy_button);
        mSMSButton = (HighlightOnPressButton) mView.findViewById(R.id.button_sms_address);
        mEmailButton = (HighlightOnPressButton) mView.findViewById(R.id.button_email_address);
        mCancelButton = (HighlightOnPressButton) mView.findViewById(R.id.fragment_qrcode_cancel_button);

        mSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSMS();
            }
        });

        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEmail();
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationActivity)getActivity()).showNavBar();
                getActivity().onBackPressed();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationActivity)getActivity()).showNavBar();
                getActivity().onBackPressed();
            }
        });

        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.showHelpInfo(getActivity(), "Info", "Wallet info");
            }
        });

        mCreateBitmapTask = new CreateBitmapTask();
        mCreateBitmapTask.execute();
        return mView;
    }


    private CreateBitmapTask mCreateBitmapTask;
    public class CreateBitmapTask extends AsyncTask<Void, Void, Void> {

        CreateBitmapTask() { }

        @Override
        protected Void doInBackground(Void... params) {
            mID = mCoreAPI.createReceiveRequestFor(mWallet, "", "", bundle.getString(RequestFragment.BITCOIN_VALUE));
            if(mID!=null) {
                mAddress = mCoreAPI.getRequestAddress(mWallet.getUUID(), mID);
                try{
                    // data in barcode is like bitcoin:address?amount=0.001
                    mQRBitmap = mCoreAPI.getQRCodeBitmap(mWallet.getUUID(), mID);
                    mRequestURI = mCoreAPI.getRequestURI();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            mCreateBitmapTask = null;
            mBitcoinAddress.setText(mAddress);
            if (mQRBitmap != null) {
                mQRView.setImageBitmap(mQRBitmap);
            }
        }

        @Override
        protected void onCancelled() {
            mCreateBitmapTask = null;
        }
    }



    private void startSMS() {
        mSendType = SendType.SEND_SMS;
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_SMS);
    }

    private void finishSMS(String name, String phone) {
        Intent smsIntent = new Intent(Intent.ACTION_SEND);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) //At least KitKat
        {
//            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); //Need to change the build to API 19
//
//            smsIntent.putExtra(Intent.EXTRA_TEXT, mRequestURI);
//
//            if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
//            {
//                smsIntent.setPackage(defaultSmsPackageName);
//            }
            smsIntent = new Intent(Intent.ACTION_SENDTO);
            if(mQRBitmap!=null) {
                smsIntent.setData(Uri.parse("mmsto:" + Uri.encode(phone)));
            } else {
                smsIntent.setData(Uri.parse("smsto:" + Uri.encode(phone)));
            }
        }
        else //For earlier versions, the old method
        {
            smsIntent.putExtra("address", phone);
            smsIntent.setType("text/plain");
        }
        smsIntent.putExtra("sms_body", mRequestURI);

        //TODO Hangouts does not recognize this
        if(mQRBitmap!=null) {
            mContentURL = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), mQRBitmap, mAddress, null);
            smsIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mContentURL));
        }

        startActivity(smsIntent);
    }

    private void startEmail() {
        mSendType = SendType.SEND_EMAIL;
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_EMAIL);
    }

    private void finishEmail(String name, String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Request Bitcoin");
        intent.putExtra(Intent.EXTRA_TEXT, mRequestURI);
        if(mQRBitmap!=null) {
            mContentURL = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), mQRBitmap, mAddress, null);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mContentURL));
        }

        startActivity(intent);
    }

    //code
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT_SMS) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri result = data.getData();

                    // get the id from the Uri
                    String id = result.getLastPathSegment();

                    // query the phone numbers for the selected phone number id
                    final Cursor c = getActivity().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{id}, null);

                    int phoneIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    final int phoneType = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    if(c.getCount() == 1) { // contact has a single phone number
                        // get the only phone number
                        if(c.moveToFirst()) {
                            String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            finishSMS(name, phone);
                        } else {
                            Log.w(TAG, "No Contact results");
                        }
                    } else if(c.getCount() > 1) { // contact has multiple phone numbers
                        final CharSequence[] numbers = new CharSequence[c.getCount()];

                        int i=0;
                        if(c.moveToFirst()) {
                            final String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            while(!c.isAfterLast()) { // for each phone number, add it to the numbers array
                                String type = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(this.getResources(), c.getInt(phoneType), ""); // insert a type string in front of the number
                                String number = type + ": " + c.getString(phoneIdx);
                                numbers[i++] = number;
                                c.moveToNext();
                            }
                            // build and show a simple dialog that allows the user to select a number
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Select Phone Number");
                            builder.setItems(numbers, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    String number = (String) numbers[item];
                                    int index = number.indexOf(":");
                                    number = number.substring(index + 2);
                                    finishSMS(name, number);
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.setOwnerActivity(getActivity());
                            alert.show();
                        } else Log.w(TAG, "No results");
                    }
                }
                break;
            case (PICK_CONTACT_EMAIL) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri result = data.getData();

                    // get the id from the Uri
                    String id = result.getLastPathSegment();

                    // query the phone numbers for the selected phone number id
                    final Cursor c = getActivity().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?",
                            new String[]{id}, null);

                    if(c.getCount() == 1) { // contact has a single phone number
                        // get the only phone number
                        if(c.moveToFirst()) {
                            String email = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                            String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME));
                            finishEmail(name, email);
                        } else {
                            Log.w(TAG, "No Contact results");
                        }
                    } else if(c.getCount() > 1) { // contact has multiple phone numbers
                        final CharSequence[] numbers = new CharSequence[c.getCount()];

                        int i=0;
                        if(c.moveToFirst()) {
                            final String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME));
                            while(!c.isAfterLast()) { // for each phone number, add it to the numbers array
                                String type = (String) ContactsContract.CommonDataKinds.Email.getTypeLabel(this.getResources(), c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)), ""); // insert a type string in front of the number
                                String email = type + ":" + c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                                numbers[i++] = email;
                                c.moveToNext();
                            }
                            // build and show a simple dialog that allows the user to select a number
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Select Email address");
                            builder.setItems(numbers, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    String email = (String) numbers[item];
                                    int index = email.indexOf(":");
                                    email = email.substring(index + 1);
                                    finishEmail(name, email);
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.setOwnerActivity(getActivity());
                            alert.show();
                        } else Log.w(TAG, "No results");
                    }
                }
                break;

        }
    }
}