package com.example.socialmaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.socialmaps.model.SaveSharedPreference;
import com.example.socialmaps.model.TestSender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsActivity extends AppCompatActivity {

    private static final String TAG = "FriendsActivity";

    private TestSender t;

    private TextView listHeader;

    private String followers;
    private ArrayList mItems;
    private RecyclerView mRecentRecyclerView;
    private LinearLayoutManager mRecentLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        listHeader = (TextView) findViewById(R.id.listHeader);

        initData();
        initRecyclerView();
        getFollowers();
    }


    private void getFollowers() {
        HashMap<String, String> hmap = new HashMap<String, String>();

        /*Adding elements to HashMap*/
        hmap.put("username", SaveSharedPreference.getUserName(FriendsActivity.this));

        t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/getFollowers",hmap);


        waitForFollowersResponse();

    }

    private synchronized void waitForFollowersResponse() {
        while (t.getResp()==null);
        Log.v(TAG,t.getResp());
        mItems.clear();
        if(t.getResp() == "nothingFound") {
            followers = null;
        } else {
            try {
                JSONArray users = new JSONArray(t.getResp());
                for (int i = 0; i < users.length(); i++) {
                    mItems.add(users.get(i).toString());
                }
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void initData() {
        mItems = new ArrayList<String>();
    }

    private void initRecyclerView() {
        mRecentRecyclerView = (RecyclerView) findViewById(R.id.recView);
        mRecentRecyclerView.setHasFixedSize(true);
        mRecentLayoutManager = new LinearLayoutManager(this);
        mRecentRecyclerView.setLayoutManager(mRecentLayoutManager);


        mAdapter = new RecyclerView.Adapter<FriendsActivity.CustomViewHolder>() {
            @Override
            public FriendsActivity.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.followlist
                        , viewGroup, false);
                return new FriendsActivity.CustomViewHolder(view);
            }

            @Override
            public void onBindViewHolder(FriendsActivity.CustomViewHolder viewHolder, final int i) {
                viewHolder.noticeSubject.setText((CharSequence) mItems.get(i));

                viewHolder.followButton.setText("UNFOLLOW");
                viewHolder.followButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        unfollowUser((String) mItems.get(i));
                    }
                });

            }

            @Override
            public int getItemCount() {
                return mItems.size();
            }

        };
        mRecentRecyclerView.setAdapter(mAdapter);

    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView noticeSubject;
        private Button followButton;

        public CustomViewHolder(View itemView) {
            super(itemView);

            noticeSubject = (TextView) itemView.findViewById(R.id.notice_subject);
            followButton = (Button) itemView.findViewById(R.id.followButton);
        }
    }


    private void unfollowUser(String username) {
        HashMap<String, String> hmap = new HashMap<String, String>();

        hmap.put("username", SaveSharedPreference.getUserName(FriendsActivity.this));
        hmap.put("unfollow", username);
        TestSender t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/unfollow", hmap);
        getFollowers();
        mAdapter.notifyDataSetChanged();
    }
}

