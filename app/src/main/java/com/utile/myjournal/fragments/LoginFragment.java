package com.utile.myjournal.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.utile.myjournal.R;
import com.utile.myjournal.database.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends SlideFragment implements GoogleApiClient.OnConnectionFailedListener {

    GoogleSignInOptions gso;
    GoogleApiClient mApiClient;
    private static final int RC_SIGN_IN = 1;
    SignInButton googlebutton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Uri photoUriForProfileImage;
    private boolean loggedIn = false;
    private View v;
    private ProgressBar progressBar;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.login_fragment, container, false);

        googlebutton = v.findViewById(R.id.google_button);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        progressBar = v.findViewById(R.id.progressBar2);

        googlebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                progressBar.setVisibility(View.VISIBLE);
            }
        });


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };
        return v;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.stopAutoManage(getActivity());
        mApiClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mApiClient.stopAutoManage(getActivity());
        mApiClient.disconnect();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(v.getContext(), "Succeed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        addUsertoFirebase(acct.getDisplayName(), acct.getEmail(), acct.getPhotoUrl());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loggedIn = false;
                        } else {
                            loggedIn = true;
                        }
                        // ...
                    }
                });
    }

    @Override
    public boolean canGoForward() {
        return loggedIn;
    }


    public void addUsertoFirebase(String userName, String userEmail, Uri User_PhotoUri) {
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //userRef.child(userEmail.replace(".", ",")).setValue(new User_Model(userEmail, userName));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor spe = sp.edit();
        loggedIn = true;
        String imageFileName = "Profile_Pic";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null && image.exists()) {
            photoUriForProfileImage = FileProvider.getUriForFile(getActivity(),
                    "com.utile.myjournal.fileprovider",
                    image);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap reduced_bitmap = getBitmapFromURL(String.valueOf(User_PhotoUri));
            if (reduced_bitmap != null && fileOutputStream != null) {
                reduced_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            }

        }

        Log.i(Constants.USER_EMAIL, userEmail);

        spe.putString(Constants.SIGNEDORNOT, "true");
        spe.putString(Constants.USER_NAME, userName);
        spe.putString(Constants.USER_EMAIL, userEmail);
        spe.putString(Constants.USER_KEY, userEmail.replace(".", ","));
        spe.putString(Constants.USER_IMAGE_URI, photoUriForProfileImage.toString());
        spe.apply();
    }

    public Bitmap getBitmapFromURL(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
