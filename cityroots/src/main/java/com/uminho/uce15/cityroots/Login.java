package com.uminho.uce15.cityroots;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import com.google.android.gms.common.*;
import com.google.android.gms.common.GooglePlayServicesClient.*;
import com.google.android.gms.plus.PlusClient;
import com.uminho.uce15.cityroots.data.CityRootsWebInterfaceImpl;

public class Login extends Activity implements View.OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {

    //Google Plus
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;

    private String fbUsername;
    private String fbFirstname;
    private String fbLastname;

    //Facebook
    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, state.toString());
            requestFbUsername(this, session);

        } else if (state.isClosed()) {
            Log.i(TAG, state.toString());
        }
    }

    private void requestFbUsername(final Activity parent, final Session session) {
        // Make an API call to get user data and define a
        // new callback to handle the response.
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                // Set the Textview's text to the user's name.
                                fbUsername = user.getUsername();
                                fbFirstname = user.getFirstName();
                                fbLastname = user.getLastName();
                                Log.d(TAG, "Username:" + fbUsername);


                                String email = fbUsername+"@facebook.com";
                                DataProvider dp = new DataProvider(getApplicationContext());
                                try {
                                    Log.d("SignUp", "email: " + email + "; fbUsername: " + fbUsername +"; "+
                                            "fbFirstname " + fbFirstname + "; fbLastname " + fbLastname );

                                    if( fbUsername != null && !fbUsername.equals("null") ){
                                        Log.d("Signup!", email);
                                        dp.signup(email,fbUsername,fbFirstname,fbLastname);
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(parent);
                                        prefs.edit().putString("userid",fbUsername).commit();
                                        prefs.edit().putString("userName",fbFirstname+" "+fbLastname).commit();
                                        prefs.edit().putString("service", "facebook").commit();


                                    }

                                } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
                                    noInternetConnectionError.printStackTrace();
                                }

                                Intent intent = new Intent(parent, Home.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        if (response.getError() != null) {
                            // Handle errors, will do so later.
                        }
                    }
                });
        request.executeAsync();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        mPlusClient = new PlusClient.Builder(this, this, this)
                .setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .setScopes(Scopes.PLUS_LOGIN)  // recommended login scope for social features
                        // .setScopes("profile")       // alternative basic login scope
                .build();

        // Progress bar to be displayed if the connection failure is not resolved.
        mConnectionProgressDialog = new ProgressDialog(this);


        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Log in with Google");
                tv.setTextAppearance(getApplicationContext(),R.style.com_facebook_loginview_default_style);
                break;
            }
        }

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mConnectionProgressDialog.dismiss();
        //Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        Log.d("G+Login", "User logged in:" + mPlusClient.getAccountName());

        String email = mPlusClient.getAccountName();
        if(!email.endsWith("@gmail.com"))
            email = email +"@gmail.com";
        String first_name = mPlusClient.getCurrentPerson().getName().getGivenName();
        String last_name = mPlusClient.getCurrentPerson().getName().getFamilyName();
        String user_id = email;

        DataProvider dp = new DataProvider(getApplicationContext());
        try {
            Log.d("SignUp", "email: " + email + "; gmail: " + email +"; "+
                    "first_name " + first_name + "; last_name " + last_name );


            if( first_name != null && !first_name.equals("null") ){
                Log.d("Signup!", email);
                dp.signup(email,email,first_name,last_name);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                prefs.edit().putString("userid",user_id).commit();
                prefs.edit().putString("userName",first_name+" "+last_name).commit();
                prefs.edit().putString("service", "google").commit();
            }


        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }

        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();

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
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
            mPlusClient.connect();

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
        }else if( mPlusClient.isConnected()){
            Toast.makeText(this, "User is already connected!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlusClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mConnectionProgressDialog.isShowing()) {
            // The user clicked the sign-in button already. Start to resolve
            // connection errors. Wait until onConnected() to dismiss the
            // connection dialog.
            if (result.hasResolution()) {
                try {
                    result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (IntentSender.SendIntentException e) {
                    mPlusClient.connect();
                }
            }
        }
        // Save the result and resolve the connection failure upon a user click.
        mConnectionResult = result;
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }
        super.onActivityResult(requestCode, responseCode, intent);
        uiHelper.onActivityResult(requestCode, responseCode, intent);
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG, "disconnected");
    }


 /* public void skipLogin(View view){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }*/
}