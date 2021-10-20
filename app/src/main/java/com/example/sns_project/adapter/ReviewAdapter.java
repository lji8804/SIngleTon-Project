package com.example.sns_project.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sns_project.FirebaseHelper;
import com.example.sns_project.PostInfo;
import com.example.sns_project.R;
import com.example.sns_project.UserInfo;
import com.example.sns_project.activity.PostActivity;
import com.example.sns_project.listener.OnPostListener;
import com.example.sns_project.view.ReadContentsVIew;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MainViewHolder> {
    private ArrayList<PostInfo> mDataset;
    private ArrayList<UserInfo> userDataset;
    private Activity activity;
    private FirebaseHelper firebaseHelper;
    private ArrayList<ArrayList<SimpleExoPlayer>> playerArrayListArrayList = new ArrayList<>();
    private final int MORE_INDEX = 2;
    private ImageView ivProfile;
    TextView tvTitle, tvID, tvGotoURL;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MainViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public ReviewAdapter(Activity activity, ArrayList<PostInfo> mDataset, ArrayList<UserInfo> userDataset) {
        this.activity = activity;
        this.mDataset = mDataset;
        this.userDataset = userDataset;
        firebaseHelper = new FirebaseHelper(activity);
    }

    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper.setOnPostListener(onPostListener);
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public ReviewAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        //카드뷰 클릭시 액티비티로 넘기는 이벤트
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PostActivity.class);
                intent.putExtra("postInfo", mDataset.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ivProfile = cardView.findViewById(R.id.iv_profile);
        tvTitle = cardView.findViewById(R.id.tv_title);
        tvID = cardView.findViewById(R.id.tv_id);
        tvGotoURL = cardView.findViewById(R.id.tv_gotoURL);

        PostInfo postInfo = mDataset.get(position);
        tvTitle.setText(postInfo.getTitle());
        tvGotoURL.setText(postInfo.getPlaceName());

        for (UserInfo userInfo: userDataset) {
            if(postInfo.getPublisher().equals(userInfo.getUid())){
                tvID.setText(userInfo.getName());
                Glide.with(ivProfile.getContext()).load(userInfo.getPhotoUrl()).into(ivProfile);
            }
        }


//        ReadContentsVIew readContentsVIew = cardView.findViewById(R.id.readContentsView);
//        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);
//
//        if (contentsLayout.getTag() == null || !contentsLayout.getTag().equals(postInfo)) {
//            contentsLayout.setTag(postInfo);
//            contentsLayout.removeAllViews();
//
//            readContentsVIew.setMoreIndex(MORE_INDEX);
//            readContentsVIew.setPostInfo(postInfo);
//
//            ArrayList<SimpleExoPlayer> playerArrayList = readContentsVIew.getPlayerArrayList();
//            if(playerArrayList != null){
//                playerArrayListArrayList.add(playerArrayList);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void myStartActivity(Class c, PostInfo postInfo) {
        Intent intent = new Intent(activity, c);
        intent.putExtra("postInfo", postInfo);
        activity.startActivity(intent);
    }

    public void playerStop(){
        for(int i = 0; i < playerArrayListArrayList.size(); i++){
            ArrayList<SimpleExoPlayer> playerArrayList = playerArrayListArrayList.get(i);
            for(int ii = 0; ii < playerArrayList.size(); ii++){
                SimpleExoPlayer player = playerArrayList.get(ii);
                if(player.getPlayWhenReady()){
                    player.setPlayWhenReady(false);
                }
            }
        }
    }
}