package com.alexazhu.callblocker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.alexazhu.callblocker.R;
import com.alexazhu.callblocker.blockednumber.BlockedNumber;
import com.alexazhu.callblocker.blockednumber.BlockedNumberDao;
import com.alexazhu.callblocker.blockednumber.BlockedNumberDatabase;
import com.alexazhu.callblocker.blockednumber.BlockedNumberType;
import com.alexazhu.callblocker.util.AsyncExecutorUtil;
import com.alexazhu.callblocker.util.PermissionsUtil;
import com.alexazhu.callblocker.view.BlockedNumberListAdapter;

import java.util.List;

public class ConfigurationActivity extends AppCompatActivity {
    private static final String LOG_TAG = ConfigurationActivity.class.getSimpleName();

    private List<BlockedNumber> blockedNumbers;

    private Context context;
    private PermissionsUtil permissionsUtil;
    private BlockedNumberDao blockedNumberDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getApplicationContext();

        setContentView(R.layout.activity_configuration);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) ->
            addNumber(new BlockedNumber(BlockedNumberType.EXACT_MATCH, "6505551212"))
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        permissionsUtil = new PermissionsUtil(this);
        blockedNumberDao = BlockedNumberDatabase.getInstance(this).blockedNumberDao();

        fetchAndPopulateBlockedNumbers();

        if (!permissionsUtil.checkPermissions()) {
            startActivity(new Intent(this, RequestPermissionsActivity.class));
        }
    }

    private void fetchAndPopulateBlockedNumbers() {
        AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
            blockedNumbers = blockedNumberDao.getAll();

            populateBlockedNumbers();
        });
    }

    private void populateBlockedNumbers() {
        runOnUiThread(() -> {
            RecyclerView listView = findViewById(R.id.blocked_number_list);
            BlockedNumberListAdapter adapter = new BlockedNumberListAdapter(blockedNumbers);
            listView.setAdapter(adapter);
            listView.setLayoutManager(new LinearLayoutManager(context));
            listView.addItemDecoration(new DividerItemDecoration(listView.getContext(), DividerItemDecoration.VERTICAL));
        });
    }

    private void addNumber(final BlockedNumber number) {
        if (!blockedNumbers.contains(number)) {
            blockedNumbers.add(number);
            AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
                blockedNumberDao.insert(number);
            });
        } else {
            Toast.makeText(context, "Number already added to list", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeNumber(final BlockedNumber number) {
        if (blockedNumbers.contains(number)) {
            blockedNumbers.remove(number);
            AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
                blockedNumberDao.delete(number);
            });
        }
    }
}
