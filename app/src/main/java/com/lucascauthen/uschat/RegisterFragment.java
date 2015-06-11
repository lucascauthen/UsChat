package com.lucascauthen.uschat;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;



public class RegisterFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_register, container, false);
        Button registerButton = (Button)myFragmentView.findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);
        return myFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.register_button:
                register();
                break;
        }
    }

    public void register() {
        //Variables related to the testing of a successful register
        String username = String.valueOf(((EditText) getActivity().findViewById(R.id.register_username)).getText());
        String email = String.valueOf(((EditText) getActivity().findViewById(R.id.register_email)).getText());
        String emailAgain = String.valueOf(((EditText) getActivity().findViewById(R.id.register_email_again)).getText());
        String password = String.valueOf(((EditText) getActivity().findViewById(R.id.register_password)).getText());
        String passwordAgain = String.valueOf(((EditText) getActivity().findViewById(R.id.register_password_again)).getText());

        //Disable the register button to ensure that the request is not made twice
        ((Button)getActivity().findViewById(R.id.register_button)).setEnabled(false);

        //Any error that will be displayed
        String error = "";

        if(!email.equals(emailAgain)) { //Emails to not match
            error = getString(R.string.email_mismatch_error_message);
        } else if(email.isEmpty() || emailAgain.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {
            error = getString(R.string.missing_field_error_message);
        } else if(!password.equals(passwordAgain)) { //Passwords do not match
            error = getString(R.string.password_mismatch_error_message);
        } else if(password.length() < getActivity().getResources().getInteger(R.integer.minPasswordChars)) { //Password does not meet length requirements
            error = getString(R.string.password_too_short_error_message_beginning) + getActivity().getResources().getInteger(R.integer.minPasswordChars) + getString(R.string.password_too_short_error_message_ending);
        }
        if(!error.equals("")) {
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            ((Button)getActivity().findViewById(R.id.register_button)).setEnabled(true);
        } else {
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        //Regisered Successful
                        Toast.makeText(getActivity(), "Successful registered", Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        ((Button) getActivity().findViewById(R.id.register_button)).setEnabled(true);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
