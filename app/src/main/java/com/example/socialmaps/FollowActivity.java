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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmaps.model.SaveSharedPreference;
import com.example.socialmaps.model.TestSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FollowActivity extends AppCompatActivity {

    private static final String TAG = "FollowActivity";
    private TestSender t;
    private TestSender t2;
    private EditText username;
    private Button searchUsername;
    private TextView list;

    private String followers;

    private TextView userList;

    private ArrayList mItems;
    private RecyclerView mRecentRecyclerView;
    private LinearLayoutManager mRecentLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        username = (EditText) findViewById(R.id.searchuser);
        searchUsername = (Button) findViewById(R.id.Search);
        list = (TextView) findViewById(R.id.searchlistview);
        searchUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUsers();
            }
        });

        initData();
        initRecyclerView();
        getFollowers();
    }

    public void getUsers() {
        HashMap<String, String> hmap = new HashMap<String, String>();

        /*Adding elements to HashMap*/
        //hmap.put("username", SaveSharedPreference.getUserName(FollowActivity.this));
        hmap.put("username", username.getText().toString().trim());
        t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/findPerson", hmap);

        waitForResponse();
    }

    private synchronized void waitForResponse() {
        while (t.getResp() == null) ;
        String resp = t.getResp();

        mItems.clear();

        if (resp.equals("nothingFound")) {
            list.setText("No one is found");
            mAdapter.notifyDataSetChanged();
        } else {
            list.setText("found ");
            try {
                JSONObject users = new JSONObject(t.getResp());
                for (int i = 0; i < users.names().length(); i++) {
                    mItems.add(users.get(users.names().getString(i)).toString());
                }
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void getFollowers() {
        HashMap<String, String> hmap = new HashMap<String, String>();

        /*Adding elements to HashMap*/
        hmap.put("username", SaveSharedPreference.getUserName(FollowActivity.this));

        t2 = new TestSender();
        t2.doThePost("http://socialmaps.openode.io/api/getFollowers",hmap);


        waitForFollowersResponse();

    }

    private synchronized void waitForFollowersResponse() {
        while (t2.getResp()==null);
        Log.v(TAG,t2.getResp());

        if(t2.getResp() == "nothingFound") {
            followers = null;
        } else {
            followers = t2.getResp();
        }

        Log.v(TAG,"You follow: " + followers);
    }

    private void initData() {
        mItems = new ArrayList<String>();
    }

    private void initRecyclerView() {
        mRecentRecyclerView = (RecyclerView) findViewById(R.id.recView);
        mRecentRecyclerView.setHasFixedSize(true);
        mRecentLayoutManager = new LinearLayoutManager(this);
        mRecentRecyclerView.setLayoutManager(mRecentLayoutManager);


        mAdapter = new RecyclerView.Adapter<CustomViewHolder>() {
            @Override
            public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.followlist
                        , viewGroup, false);
                return new CustomViewHolder(view);
            }

            @Override
            public void onBindViewHolder(CustomViewHolder viewHolder, final int i) {
                viewHolder.noticeSubject.setText((CharSequence) mItems.get(i));
                if(followers.contains("\"" + (CharSequence) mItems.get(i) + "\"")) {
                    viewHolder.followButton.setText("UNFOLLOW");
                    viewHolder.followButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            unfollowUser((String) mItems.get(i));
                        }
                    });
                } else {
                    viewHolder.followButton.setText("FOLLOW");
                    viewHolder.followButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            followUser((String) mItems.get(i));
                        }
                    });
                }
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

    private void followUser(String username) {
        HashMap<String, String> hmap = new HashMap<String, String>();

        hmap.put("username", SaveSharedPreference.getUserName(FollowActivity.this));
        hmap.put("follow", username);
        TestSender t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/follow", hmap);
    }

    private void unfollowUser(String username) {
        HashMap<String, String> hmap = new HashMap<String, String>();

        hmap.put("username", SaveSharedPreference.getUserName(FollowActivity.this));
        hmap.put("unfollow", username);
        TestSender t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/unfollow", hmap);
    }
}
