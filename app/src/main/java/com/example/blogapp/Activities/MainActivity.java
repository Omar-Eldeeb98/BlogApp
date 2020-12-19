package com.example.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blogapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button sign_in_btn;
    private Button sign_up_btn;
    private EditText email_edit_text;
    private EditText password_edit_text;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        sign_in_btn = (Button) findViewById( R.id.login_btn );
        sign_up_btn = (Button) findViewById( R.id.create_account_btn );
        email_edit_text = (EditText) findViewById( R.id.email_edit_text );
        password_edit_text = (EditText) findViewById( R.id.password_edit_text );

        sign_up_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( MainActivity.this , CreateAccount.class ) );
                finish();
            }
        } );


        //..............................................................................
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);                           //................
        toolbar.inflateMenu(R.menu.main_menu);                                             //................
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {           //................
            @Override                                                                        //................
            public boolean onMenuItemClick(MenuItem item) {                                   //................
                if (item.getItemId() == R.id.action_signOut)
                {                                    //................

                    mAuth.signOut();
                    email_edit_text.setText( "" );      //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                    password_edit_text.setText( "" );  //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                     Toast.makeText( MainActivity.this , "Signed Out" , Toast.LENGTH_LONG ).show();

                }
                return false;
            }
        });
        //..........................................................................................







        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               user =  firebaseAuth.getCurrentUser();
                if (user != null)
                {
                  // Toast.makeText( MainActivity.this , "Signed In" , Toast.LENGTH_LONG ).show();
                   Log.d(TAG , "user signed in ");
                  startActivity( new Intent( MainActivity.this , PostListActivity.class ) );
                  finish();


                }
                else
                {
                   // Toast.makeText( MainActivity.this , "Signed Out ........" , Toast.LENGTH_LONG ).show();
                    Log.d(TAG , "user signed out ");
                }
            }
        };

        // to make the user log in to his account in the application .....
        sign_in_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email_edit_text.getText().toString())
                        &&!TextUtils.isEmpty( password_edit_text.getText().toString() ))
                {
                    String email = email_edit_text.getText().toString();
                    String pwd = password_edit_text.getText().toString();
                    login(email , pwd);
                }
                else
                {

                    Toast.makeText( MainActivity.this , "انت مستخدم اهبل !!!" , Toast.LENGTH_LONG ).show();
                    // .................... ..................
                }
            }
        } );


    }

    private void login(String email, String pwd) {

        mAuth.signInWithEmailAndPassword( email , pwd ).addOnCompleteListener( MainActivity.this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    // we are in
                    Toast.makeText( MainActivity.this , "Signed in ..." , Toast.LENGTH_LONG ).show();
                   startActivity( new Intent( MainActivity.this , PostListActivity.class ) );


                }
                else
                {
                    // not in !! .....
                    Toast.makeText( MainActivity.this , "Failed Sign in !" , Toast.LENGTH_LONG ).show();

                }
            }
        } );
    }

    // menu ......................................................
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() ==R.id.action_signOut)
        {
           // mAuth.signOut();
        //  Toast.makeText( MainActivity.this , "Signed Out" , Toast.LENGTH_LONG ).show();
        }
        return super.onOptionsItemSelected( item );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main_menu , menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener( mAuthStateListener );
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null)
        {
            mAuth.removeAuthStateListener( mAuthStateListener );
        }

    }


}

