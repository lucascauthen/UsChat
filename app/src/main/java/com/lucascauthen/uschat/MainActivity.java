package com.lucascauthen.uschat;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gets rid of tittle bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //assign fragmentManager reference
        this.fragmentManager = getFragmentManager();

        //Parse initialization

        Parse.initialize(this, "6BYovscrrL3h55LXMWgQdQ4AYm7dBQ6vO0BjYZ1e", "e7iTzIwBjs0pmGo7Kq36esdeqLKxHbFg3IIRkvGD");

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null) {

        } else {
            //The user is not logged in
            addLoginFragment();
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void addLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.group, loginFragment, "login");
        transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_in);
        transaction.commit();
    }
    public void showRegisterFragment() {
        RegisterFragment registerFragment = (RegisterFragment)fragmentManager.findFragmentByTag("register");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(registerFragment == null) {
            registerFragment = new RegisterFragment();
        }
        transaction.detach(fragmentManager.findFragmentByTag("login"));
        transaction.add(R.id.group, registerFragment);
        transaction.addToBackStack("register");
        transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_in);
        transaction.commit();
    }
    public void showHomeFragment(){
        HomeFragment homeFragment = (HomeFragment)fragmentManager.findFragmentByTag("home");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        transaction.detach(fragmentManager.findFragmentByTag("home"));
        transaction.add(R.id.group, homeFragment);
        //Clear the back stack because we don't want you to be able to go back the the register screen/loggin screen again if you press back
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        transaction.addToBackStack("home");
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
