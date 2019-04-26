package com.example.android.hindutemple;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.android.hindutemple.model.Temples;
import com.example.android.hindutemple.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TempleDetailActivity extends AppCompatActivity {

    private Toolbar mTopToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private ArrayList<Temples> mTempleDetails;
    private Temples templeInfo;
    private ImageView mBackgroundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_detail);

        mTempleDetails = new ArrayList<>();

        mTopToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mTopToolbar);

        mCollapsingToolbarLayout = findViewById(R.id.main_collapsing_layout);
        mBackgroundImageView = findViewById(R.id.iv_main_backdrop);

        templeInfo = getIntent().getParcelableExtra(Constants.TEMPLES_INFO);

        if(templeInfo != null) {
            mCollapsingToolbarLayout.setTitle(templeInfo.getTempleName());
            Picasso.with(this)
                    .load(templeInfo.getTempleImageUri())
                    .placeholder(R.drawable.temple_image)
                    .error(R.drawable.temple_image)
                    .into(mBackgroundImageView);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.admin_login:
                Intent intent = new Intent(this, AdminLoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
