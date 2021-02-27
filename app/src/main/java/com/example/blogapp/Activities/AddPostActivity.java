package com.example.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.blogapp.Model.Blog;
import com.example.blogapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private ImageButton mPostImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitButton;
    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mProgress;

    private Uri mImageUri; // to choose an image from the gallery of your phone ...
    private static final int GALLERY_CODE = 1; // to choose an image from the gallery of your phone ...

    private StorageReference mStorage;    // to upload image to firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_post );

        mProgress = new ProgressDialog( this );


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mStorage = FirebaseStorage.getInstance().getReference(); // to upload image to firebase

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child( "MBlog" );


        mPostImage = (ImageButton) findViewById( R.id.imageButton );
        mPostTitle = (EditText) findViewById( R.id.post_title_et );
        mPostDesc = (EditText) findViewById( R.id.description_et );
        mSubmitButton = (Button) findViewById( R.id.submit_post_btn );

        //_________________________________to choose an image from the gallery of your phone ...__________________________________

        mPostImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent( Intent.ACTION_GET_CONTENT );
                galleryIntent.setType( "image/*" );
                startActivityForResult( galleryIntent, GALLERY_CODE );

            }
        } );

        //_______________________________to choose an image from the gallery of your phone ...______________________________________

        mSubmitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // posting to database
                startPosting();

            }
        } );


    }

    //_______________________________to choose an image from the gallery of your phone ...______________________________________


    @Override
    public void onBackPressed() {
        Intent intent = new Intent( AddPostActivity.this , PostListActivity.class );
        startActivity( intent );
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mPostImage.setImageURI( mImageUri );

        }
    }
    //_______________________________to choose an image from the gallery of your phone ...______________________________________

    private void startPosting() {
        mProgress.setMessage( "posting to Blogy" );
        mProgress.show();

        final String titleVal = mPostTitle.getText().toString().trim();
        final String descVal = mPostDesc.getText().toString().trim();

        if (!TextUtils.isEmpty( titleVal ) && !TextUtils.isEmpty( descVal )
        && mImageUri != null)
        {
            // start the uploading ......
            StorageReference filePath = mStorage.child( "MBlog_images" )
                    .child( mImageUri.getLastPathSegment() );
            filePath.putFile( mImageUri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener( new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            DatabaseReference  newPost = mPostDatabase.push();
                            Map<String , String> dataToSave  = new HashMap<>();
                            dataToSave.put( "title" , titleVal);
                            dataToSave.put( "desc" , descVal);
                            dataToSave.put( "image" , String.valueOf(task.getResult()));
                            dataToSave.put( "timestamp" , String.valueOf( java.lang.System.currentTimeMillis() ));
                            dataToSave.put( "userId" , mUser.getUid());



                            newPost.setValue( dataToSave );
                            mProgress.dismiss();
                            startActivity( new Intent( AddPostActivity.this , PostListActivity.class ) );
                            finish();


                        }
                    } );
                }
            } );


        }
        else
        {
            Toast.makeText( getApplicationContext() , "اكتب كسم البوست , انت عايز دين امى يعنى انشرلك بوست فاضى !!! دا انت مستخدم خنزير !!!!!"  ,Toast.LENGTH_LONG ).show();
            mProgress.dismiss();
        }
    }
}