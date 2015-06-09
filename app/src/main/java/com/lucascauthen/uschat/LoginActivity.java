package com.lucascauthen.uschat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;


import com.lucascauthen.uschat.Animations.Animations;
import com.lucascauthen.uschat.Managers.FragmentTransitionManager;
import com.parse.Parse;
import com.parse.ParseUser;


public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private final FragmentTransitionManager transitionManager = new FragmentTransitionManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gets rid of tittle bar

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //assign fragmentManager reference
        this.fragmentManager = getFragmentManager();

        //Parse initialization

        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser != null) {
            startMainActivity();
        } else {
            //The user is not logged in
            addLoginFragment();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void addLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        //TODO: Fix FADE and add it here
        transitionManager.replaceFragment(loginFragment, getString(R.string.login_fragment_name), "");

    }
    public void showRegisterFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag(getString(R.string.register_fragment_name));
        if(fragment == null) {
            fragment = new RegisterFragment();
        }
        transitionManager.replaceFragment(fragment, getString(R.string.register_fragment_name), getString(R.string.login_fragment_name), Animations.SLIDE_FROM_LEFT.getAnim());
    }

    public void startMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //This is 1 because 0 whould mean that pressing the back button would end you back to the blank activity without any fragment
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}
