package com.example.blogapp.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogapp.Model.Blog;
import com.example.blogapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {
   // fields of the class of the adapter ....
    private Context context;
    private List<Blog> blogList;

      // constructor .....
    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.post_row , parent , false );
        return new ViewHolder( view , context );
    }

    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerAdapter.ViewHolder holder, int position) {

        Blog blog = blogList.get( position );
        String imageUrl = null;


        holder.title.setText( blog.getTitle() );
        holder.desc.setText( blog.getDesc() );

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date( Long.valueOf( blog.getTimestamp() ) ).getTime() );
        holder.timeStamp.setText( formattedDate );

        imageUrl = blog.getImage();

        // todo use picasso lib to load images .....
       //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
        Picasso.get().load( imageUrl ).into( holder.image );
    }



    @Override
    public int getItemCount() {

        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView desc;
        public TextView timeStamp;
        public ImageView image;
        String userId;

        public ViewHolder(@NonNull View view , Context ctx)   {
            super( view );

            context = ctx;
            title  = (TextView) view.findViewById( R.id.postTitleList );
            desc = (TextView) view.findViewById( R.id.postTextList );
            timeStamp = (TextView) view.findViewById( R.id.timeStampList );
            image = (ImageView) view.findViewById( R.id.postImageList );
            userId = null;

            view.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // we can go to the next activity ....
                }
            } );

        }
    }

}
