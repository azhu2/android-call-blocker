package com.alexazhu.callblocker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
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

    private boolean actionButtonsVisible = false;
    private BlockedNumberListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getApplicationContext();

        setContentView(R.layout.activity_configuration);

        setupButtons();
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
            listAdapter = new BlockedNumberListAdapter(blockedNumbers);
            listView.setAdapter(listAdapter);
            listView.setLayoutManager(new LinearLayoutManager(context));
            listView.addItemDecoration(new DividerItemDecoration(listView.getContext(), DividerItemDecoration.VERTICAL));
        });
    }

    private void addNumber(final BlockedNumber number) {
        AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
            if (!blockedNumbers.contains(number)) {
                blockedNumberDao.insert(number);
                blockedNumbers.add(number);
                listAdapter.notifyDataSetChanged();
            } else {
                this.runOnUiThread(() -> Toast.makeText(context, "Number already added to list", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void removeNumber(final BlockedNumber number) {
        AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
            if (blockedNumbers.contains(number)) {
                blockedNumbers.remove(number);
                listAdapter.notifyDataSetChanged();
                blockedNumberDao.delete(number);
                this.runOnUiThread(() -> Toast.makeText(context, "Number deleted", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void setupButtons() {
        FloatingActionButton mainFab = findViewById(R.id.main_fab);

        FloatingActionButton exactFab = findViewById(R.id.exact_fab);
        TextView exactLabel = findViewById(R.id.exact_label);
        FloatingActionButton regexFab = findViewById(R.id.regex_fab);
        TextView regexLabel = findViewById(R.id.regex_label);

        // Toggle action button visibility from main button
        mainFab.setOnClickListener((view) -> {
            actionButtonsVisible = !actionButtonsVisible;

            // Ugly if-then because FloatingActionButton.setVisibility() can't be called here
            if (actionButtonsVisible) {
                exactFab.show();
                exactLabel.setVisibility(View.VISIBLE);
                regexFab.show();
                regexLabel.setVisibility(View.VISIBLE);
            } else {
                exactFab.hide();
                exactLabel.setVisibility(View.GONE);
                regexFab.hide();
                regexLabel.setVisibility(View.GONE);
            }
        });

        // TODO Add real functionality
        View.OnClickListener openExactDialog = (view) -> {
            addNumber(new BlockedNumber(BlockedNumberType.EXACT_MATCH, "6505551212"));
        };
        exactFab.setOnClickListener(openExactDialog);
        exactLabel.setOnClickListener(openExactDialog);

        View.OnClickListener openRegexDialog = (view) -> {
            addNumber(new BlockedNumber(BlockedNumberType.REGEX_MATCH, "240426"));
        };
        regexFab.setOnClickListener(openRegexDialog);
        regexLabel.setOnClickListener(openRegexDialog);
    }
}
