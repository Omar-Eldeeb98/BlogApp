package com.example.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.blogapp.Data.BlogRecyclerAdapter;
import com.example.blogapp.Model.Blog;
import com.example.blogapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PostListActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;

    private RecyclerView recyclerView;// ......................... to show the posts in recyclerView
    private BlogRecyclerAdapter blogRecyclerAdapter; // ........................... to show the posts in recyclerView
    private List<Blog> blogList;// ............ to show the posts in recyclerView



    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_post_list );

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child( "MBlog" );
        mDatabaseReference.keepSynced( true );


        blogList = new ArrayList<>(); // .......................... to show the posts in recyclerView
        recyclerView = (RecyclerView) findViewById( R.id.recyclerView );  // .......................... to show the posts in recyclerView
        recyclerView.setHasFixedSize( true );  // .......................... to show the posts in recyclerView
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) ); // .......................... to show the posts in recyclerView




        //..............................................................................
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);                           //................
        toolbar.inflateMenu(R.menu.main_menu);                                             //................
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {           //................
            @Override                                                                        //................
            public boolean onMenuItemClick(MenuItem item) {                                   //................
                if (item.getItemId() == R.id.action_signOut)
                {
                    if (mUser != null && mAuth !=null)
                    {
                        mAuth.signOut();
                        startActivity( new Intent( PostListActivity.this , MainActivity.class ) );
                        finish();

                    }



                }
                if (item.getItemId() ==R.id.action_add)
                {
                    if (mUser != null && mAuth !=null)
                    {
                        startActivity( new Intent( PostListActivity.this , AddPostActivity.class ) );
                        finish();

                    }


                }


                    return false;
            }
        });
        //..........................................................................................

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main_menu , menu);
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
   /*
        switch (item.getItemId())
        {
            case R.id.action_add:
            if (mUser != null && mAuth !=null)
            {
                startActivity( new Intent( PostListActivity.this , AddPostActivity.class ) );
                finish();

            }
            break;
            case R.id.action_signOut:
                if (mUser != null && mAuth !=null)
                {
                    mAuth.signOut();
                    startActivity( new Intent( PostListActivity.this , MainActivity.class ) );
                    finish();

                }
                break;

        }

    */

        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                //_________________________________________



                //______________________________________-



                Blog blog =  snapshot.getValue(Blog.class); // بيجيب كل الداتا الى متخزنه ف الفايربيز ويحطها ك باراميترز ل blog object
              //  blogList.clear(); //Clear your array list before adding new data in it
                blogList.add( blog );
                Collections.reverse( blogList );
                blogRecyclerAdapter = new BlogRecyclerAdapter( PostListActivity.this , blogList );
                recyclerView.setAdapter( blogRecyclerAdapter );
                blogRecyclerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }

    @Override
    protected void onStop() {
        super.onStop();
        blogList.clear();

    }
}