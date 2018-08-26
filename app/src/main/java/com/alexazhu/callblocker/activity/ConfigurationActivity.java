package com.alexazhu.callblocker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alexazhu.callblocker.R;
import com.alexazhu.callblocker.blockednumber.BlockedNumber;
import com.alexazhu.callblocker.blockednumber.BlockedNumberType;
import com.alexazhu.callblocker.util.PermissionsUtil;
import com.alexazhu.callblocker.view.BlockedNumberListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationActivity extends AppCompatActivity {
    private static final String LOG_TAG = ConfigurationActivity.class.getSimpleName();

    private final List<BlockedNumber> blockedNumbers;

    private PermissionsUtil permissionsUtil;

    public ConfigurationActivity() {
        this.permissionsUtil = new PermissionsUtil(this);

        blockedNumbers = new ArrayList<>();
        blockedNumbers.add(new BlockedNumber(BlockedNumberType.EXACT_MATCH, "6505551212"));
        blockedNumbers.add(new BlockedNumber(BlockedNumberType.REGEX_MATCH, "555555/d*$"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        RecyclerView listView = findViewById(R.id.blocked_number_list);
        BlockedNumberListAdapter adapter = new BlockedNumberListAdapter(blockedNumbers);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.addItemDecoration(new DividerItemDecoration(listView.getContext(), DividerItemDecoration.VERTICAL));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!permissionsUtil.checkPermissions()) {
            startActivity(new Intent(this, RequestPermissionsActivity.class));
        }
    }
}
