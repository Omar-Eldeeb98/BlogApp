package com.example.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.blogapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private Button createAccountBtn;

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_create_account);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child( "MUsers" );

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog( this );

        firstName = (EditText) findViewById( R.id.first_name_act );
        lastName = (EditText) findViewById( R.id.last_name_act );
        email = (EditText) findViewById( R.id.email_act );
        password = (EditText) findViewById( R.id.password_act );
        createAccountBtn = (Button) findViewById( R.id.create_act_btn );
        createAccountBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup process is here .
                createNewAccount();
            }
        } );

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( CreateAccount.this , MainActivity.class );
        startActivity( intent );
        super.onBackPressed();
    }

    private void createNewAccount() {

        final String name = firstName.getText().toString().trim();
        final String lName = lastName.getText().toString().trim();
        String em = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        if (!TextUtils.isEmpty( name )&&!TextUtils.isEmpty( lName )
                &&!TextUtils.isEmpty( em )&&!TextUtils.isEmpty( pwd ))
        {
            mProgressDialog.setMessage( "Creating Account ..." );
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword( em , pwd ).addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if (authResult != null)
                    {
                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserIdDb =  mDatabaseReference.child( userId );
                        currentUserIdDb.child( "firstName" ).setValue( name );
                        currentUserIdDb.child( "lastName" ).setValue( lName );
                        currentUserIdDb.child( "image" ).setValue( "none" );

                        mProgressDialog.dismiss();


                        // send user to post list screen
                        Intent intent = new Intent( CreateAccount.this , PostListActivity.class );
                        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP  );
                        startActivity( intent );





                    }
                }
            } );



        }



    }


}