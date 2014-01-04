package com.uminho.uce15.cityroots;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.widget.LoginButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;

public class Login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, View.OnClickListener{

        private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
        private ProgressDialog mConnectionProgressDialog;
        private PlusClient mPlusClient;
        private ConnectionResult mConnectionResult = null;


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {


            mPlusClient = new PlusClient.Builder(getActivity(), this, this)
                    .setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                    .build();


            mConnectionProgressDialog = new ProgressDialog(getActivity());
            mConnectionProgressDialog.setMessage("Signing in...");

            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            SignInButton signInButton = (SignInButton) rootView.findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(this);

            return rootView;
        }


        @Override
        public void onStart() {
            super.onStart();
            mPlusClient.connect();
        }

        @Override
        public void onStop() {
            super.onStop();
            mPlusClient.disconnect();
        }

        @Override
        public void onClick(View view) {

            if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
                if (mConnectionResult == null) {
                    mConnectionProgressDialog.show();

                } else {
                    try {
                        mConnectionResult.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLVE_ERR);
                    } catch (IntentSender.SendIntentException e) {
                        // Try connecting again.
                        mConnectionResult = null;
                        mPlusClient.connect();
                    }
                }
            }

            //Intent intent = new Intent(getActivity(), Home.class);
            //this.startActivity(intent);
        }


        @Override
        public void onConnected(Bundle bundle) {
            mConnectionProgressDialog.dismiss();
            Toast.makeText(getActivity(), "User is connected!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onConnectionFailed(ConnectionResult result) {
            if (result.hasResolution()) {
                // The user clicked the sign-in button already. Start to resolve
                // connection errors. Wait until onConnected() to dismiss the
                // connection dialog.
                try {
                    result.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLVE_ERR);
                } catch (IntentSender.SendIntentException e) {
                    mPlusClient.disconnect();
                    mPlusClient.connect();
                }
            }
        }


        @Override
        public void onActivityResult(int requestCode, int responseCode, Intent intent) {
            if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
                mConnectionResult = null;
                mPlusClient.disconnect();
                mPlusClient.connect();
            }
        }
    }

    public void login(View view) {
        // Do something in response to button click
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
        prefs.edit().putString("username", ((EditText) findViewById(R.id.email_address)).getText().toString()).commit();

        Intent intent = new Intent(this, Home.class);
        this.startActivity(intent);
    }

    public void registar(View view) {
        Intent intent = new Intent(this, Register.class);
        //myIntent.putExtra("key", value); //Optional parameters
        this.startActivity(intent);
        //setContentView(R.layout.fragment_register);
    }

    /*public void googleLogin(View view){
        if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
            if (mConnectionResult == null) {
                mConnectionProgressDialog.show();
            } else {
                try {
                    mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (IntentSender.SendIntentException e) {
                    // Try connecting again.
                    mConnectionResult = null;
                    mPlusClient.connect();
                }
            }
        }
    }*/

}
