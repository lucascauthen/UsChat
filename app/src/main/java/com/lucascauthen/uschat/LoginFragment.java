package com.lucascauthen.uschat;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener  {


    private OnFragmentInteractionListener mListener;

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        Button loginButton = (Button)myFragmentView.findViewById(R.id.login_form_sign_in);
        Button signupButton = (Button)myFragmentView.findViewById(R.id.signup_button);
        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
        // Inflate the layout for this fragment
        return myFragmentView;
    }


    @Override
    public void onStart() {
        super.onStart();
        ((LoginActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((LoginActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        ((LoginActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch(v.getId()) {
            case R.id.signup_button:
                ((LoginActivity)getActivity()).showRegisterFragment();
                break;
            case R.id.login_form_sign_in:
                getActivity().findViewById(R.id.login_form_sign_in).setEnabled(false);
                login();
                break;
        }
    }
    public void login() {
        String username = String.valueOf(((EditText)getActivity().findViewById(R.id.login_form_email)).getText());
        String password = String.valueOf(((EditText)getActivity().findViewById(R.id.login_form_password)).getText());
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(parseUser != null) {
                    ((LoginActivity)getActivity()).startMainActivity();
                } else {
                    //Invalid username or passowrd
                    Toast.makeText(getActivity(), getString(R.string.unsuccessful_login_error), Toast.LENGTH_SHORT).show();
                }
                getActivity().findViewById(R.id.login_form_sign_in).setEnabled(true);
            }
        });
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
