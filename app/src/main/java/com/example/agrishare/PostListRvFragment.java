package com.example.agrishare;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrishare.model.Model;
import com.example.agrishare.model.ModelFireBase;
import com.example.agrishare.model.Post;

import java.util.List;

public class PostListRvFragment extends Fragment {
    List<Post> data;
    ProgressBar progressBar;
    MyAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list,container,false);
        progressBar = view.findViewById(R.id.postslist_progressbar);
        progressBar.setVisibility(view.GONE);
        RecyclerView list = view.findViewById(R.id.postslist_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        list.setAdapter(adapter);

        Model.instance.getAllPosts((modelList)->{
            Log.d("tag", "Got list from model");
            data = modelList;
            adapter.notifyDataSetChanged();});


//        ModelFireBase.getAllPosts((modelList)->{
//            Log.d("tag", "Got list from model");
//            data = modelList;
//            adapter.notifyDataSetChanged();});


        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
               Long stId = data.get(position).getId();
               Navigation.findNavController(v).navigate(PostListRvFragmentDirections.actionPostListRvFragmentToPostDetailsFragment(stId));
            //    Navigation.findNavController(v).navigate();
            }
        });


        ImageButton add = view.findViewById(R.id.postlist_add_btn);
        add.setOnClickListener((v)->{ Navigation.findNavController(v).navigate(R.id.action_postListRvFragment_to_addPostFragment); });
        setHasOptionsMenu(true);
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh(){
        Model.instance.getAllPosts((list)->{
            Log.d("tag", "Got list from model");
            data = list;
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView TitleTv;
        TextView AddressTv;
        TextView PostTv;
        TextView PriceTv;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            TitleTv = itemView.findViewById(R.id.listrowTitle);
            AddressTv = itemView.findViewById(R.id.listrowAddress);
            PostTv = itemView.findViewById(R.id.listrowPost);
            PriceTv = itemView.findViewById(R.id.listrowPrice);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                Log.d("tag","myview holder - position");
                Log.d("tag",String.valueOf(pos));
                listener.onItemClick(v,pos);
            });
        }
    }

    interface OnItemClickListener{
        void onItemClick(View v,int position);
    }
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        OnItemClickListener listener;
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_list_row,parent,false);
            return new MyViewHolder(view,listener);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post post = data.get(position);
            holder.TitleTv.setText(post.getTitle());
            holder.AddressTv.setText(post.getAddress());
            holder.PostTv.setText(post.getPost());
            holder.PriceTv.setText(post.getPrice());
        }

        @Override
        public int getItemCount() {
            if(data == null)
                return 0;
            return data.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.post_list_menu,menu);
    }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addPost_menu){
            Log.d("TAG","ADD...");
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}