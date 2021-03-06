package com.example.agrishare.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.agrishare.R;
import com.example.agrishare.login.LoginActivity;
import com.example.agrishare.model.Model;
import com.example.agrishare.model.Post;
import com.squareup.picasso.Picasso;

public class PostListRvFragment extends Fragment {
    MyAdapter adapter;
    PostListRvViewModel viewModel;
    SwipeRefreshLayout swipeRefresh;
    View view;
    ImageButton logoutBtn;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostListRvViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_posts_list, container, false);
        RecyclerView list = view.findViewById(R.id.postslist_rv);
        list.setHasFixedSize(true);
        swipeRefresh = view.findViewById(R.id.postlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(Model.instance::refreshPostList);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        list.setAdapter(adapter);
        adapter.setOnItemClickListener((v, position) -> {
            Long Id = viewModel.getData().getValue().get(position).getId();
            Navigation.findNavController(v).navigate(PostListRvFragmentDirections.actionPostListRvFragmentToPostDetailsFragment(Id));
        });
        ImageButton add = view.findViewById(R.id.postlist_add_btn);
        add.setOnClickListener((v) -> Navigation.findNavController(v).navigate(R.id.action_postListRvFragment_to_addPostFragment));
        logoutBtn = view.findViewById(R.id.postlist_logoutButton);
        logoutBtn.setOnClickListener(v -> logout());
        view.findViewById(R.id.postlist_userButton).setOnClickListener
                (Navigation.createNavigateOnClickListener
                        (R.id.action_postListRvFragment_to_userDetailsFragment));
        setHasOptionsMenu(true);
        viewModel.getData().observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(Model.instance.getPostListLoadingState().getValue() == Model.PostListLoadingState.loading);
        Model.instance.getPostListLoadingState().observe(getViewLifecycleOwner(), studentListLoadingState -> swipeRefresh.setRefreshing(studentListLoadingState == Model.PostListLoadingState.loading));

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh(){
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    private void logout() {
        Model.instance.logout(() -> toLoginActivity());
    }

    private void toLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView TitleTv;
        TextView AddressTv;
        TextView PostTv;
        TextView PriceTv;
        ImageView avatar;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            TitleTv = itemView.findViewById(R.id.listrowTitle);
            AddressTv = itemView.findViewById(R.id.listrowAddress);
            PostTv = itemView.findViewById(R.id.listrowPost);
            PriceTv = itemView.findViewById(R.id.listrowPrice);
            avatar = itemView.findViewById(R.id.listrow_avatar_imv);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v,pos);
            });
        }
        public void bind(Post post){
            TitleTv.setText(post.getTitle());
            AddressTv.setText(post.getAddress());
            PostTv.setText(post.getPost());
            PriceTv.setText(post.getPrice());
            avatar.setImageResource(R.drawable.default_post_picture);
            if(!post.getAvatarUrl().equals(""))
                Picasso.get().load(post.getAvatarUrl()).into(avatar);
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
            Post post = viewModel.getData().getValue().get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            if(viewModel.getData().getValue() == null){ return 0; }
            return viewModel.getData().getValue().size();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.post_list_menu,menu);
    }

   /*@Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addPost_menu)
            return true;
        else
            return super.onOptionsItemSelected(item);
    }
    */
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       return super.onOptionsItemSelected(item);
   }
}