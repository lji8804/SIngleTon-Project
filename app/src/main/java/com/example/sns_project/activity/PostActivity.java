package com.example.sns_project.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.sns_project.FirebaseHelper;
import com.example.sns_project.PostInfo;
import com.example.sns_project.R;
import com.example.sns_project.listener.OnPostListener;
import com.example.sns_project.view.ReadContentsVIew;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.annotation.NonNull;

public class PostActivity extends BasicActivity {
    private PostInfo postInfo;
    private FirebaseHelper firebaseHelper;
    private ReadContentsVIew readContentsVIew;
    private LinearLayout contentsLayout;
    private Button btnKakaoplace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postInfo = (PostInfo) getIntent().getSerializableExtra("postInfo");
        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsVIew = findViewById(R.id.readContentsView);
        btnKakaoplace = findViewById(R.id.btn_kakaoplace);
        final String url = postInfo.getPlaceUrl();

        btnKakaoplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        if(postInfo.getCategory().equals("reviews")){
            btnKakaoplace.setVisibility(View.GONE);
        }

        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);
        uiUpdate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    postInfo = (PostInfo)data.getSerializableExtra("postinfo");
                    contentsLayout.removeAllViews();
                    uiUpdate();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();
        String writer = postInfo.getPublisher();

        if(currentUid.equals(writer)){
            switch (item.getItemId()) {
                case R.id.delete:
                    firebaseHelper.storageDelete(postInfo);
                    return true;
                case R.id.modify:
                    myStartActivity(WritePostActivity.class, postInfo);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }else{
            Toast.makeText(getApplicationContext(), "????????? ????????????", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostInfo postInfo) {
            Log.e("?????? ","?????? ??????");
        }

        @Override
        public void onModify() {
            Log.e("?????? ","?????? ??????");
        }
    };

    private void uiUpdate(){
        setToolbarTitle(postInfo.getTitle());
        readContentsVIew.setPostInfo(postInfo);
        FirebaseUser user;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();;
        user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();
        CollectionReference collectionReference = firebaseFirestore.collection(postInfo.getCategory());
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //????????? ????????? ?????? ?????? ????????? ????????????.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //document.getData() or document.getId() ?????? ?????? ????????????
                        //???????????? ????????? ??? ??????.
                        document.get("publisher");
                    }
                    //????????? ?????????
                }
            }
        });
    }

    private void myStartActivity(Class c, PostInfo postInfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", postInfo);
        intent.putExtra("collectionPath", postInfo.getCategory());
        startActivityForResult(intent, 0);
    }
}
