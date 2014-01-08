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

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
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

        private static final String TAG = "MainFragment";

        private Session.StatusCallback callback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }
        };

        private UiLifecycleHelper uiHelper;

        public PlaceholderFragment() {
        }

        private void onSessionStateChange(Session session, SessionState state, Exception exception) {
            if (state.isOpened()) {
                Log.i(TAG, state.toString());
            } else if (state.isClosed()) {
                Log.i(TAG, state.toString());
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            uiHelper = new UiLifecycleHelper(getActivity(), callback);
            uiHelper.onCreate(savedInstanceState);
        }

        @Override
        public void onResume() {
            super.onResume();
            // For scenarios where the main activity is launched and user
            // session is not null, the session state change notification
            // may not be triggered. Trigger it if it's open/closed.
            Session session = Session.getActiveSession();
            if (session != null &&
                    (session.isOpened() || session.isClosed()) ) {
                onSessionStateChange(session, session.getState(), null);
            }

            uiHelper.onResume();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            uiHelper.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onPause() {
            super.onPause();
            uiHelper.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            uiHelper.onDestroy();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            uiHelper.onSaveInstanceState(outState);
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

            LoginButton authButton = (LoginButton) rootView.findViewById(R.id.authButton);
            authButton.setFragment(this);


            return rootView;
        }

/*
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
*/
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
