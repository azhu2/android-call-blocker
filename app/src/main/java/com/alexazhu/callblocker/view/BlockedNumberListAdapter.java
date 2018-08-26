package com.alexazhu.callblocker.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexazhu.callblocker.R;
import com.alexazhu.callblocker.blockednumber.BlockedNumber;

import java.util.List;

public class BlockedNumberListAdapter extends RecyclerView.Adapter<BlockedNumberListAdapter.BlockedNumberViewHolder> {
    private final List<BlockedNumber> blockedNumbers;

    public static class BlockedNumberViewHolder extends RecyclerView.ViewHolder {
        private TextView matchTypeView;
        private TextView phoneNumberView;

        private BlockedNumberViewHolder(@NonNull final View itemView) {
            super(itemView);

            matchTypeView = itemView.findViewById(R.id.match_type);
            phoneNumberView = itemView.findViewById(R.id.phone_number);
        }
    }

    public BlockedNumberListAdapter(List<BlockedNumber> blockedNumbers) {
        this.blockedNumbers = blockedNumbers;
    }

    @NonNull
    @Override
    public BlockedNumberViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int index) {
        final Context context = viewGroup.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View phoneNumberView = inflater.inflate(R.layout.item_phone_number, viewGroup, false);
        return new BlockedNumberViewHolder(phoneNumberView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BlockedNumberViewHolder viewHolder, final int index) {
        final BlockedNumber number = blockedNumbers.get(index);
        viewHolder.matchTypeView.setText(number.getType().getDisplayText());
        viewHolder.phoneNumberView.setText(number.getPattern());
    }

    @Override
    public int getItemCount() {
        return blockedNumbers.size();
    }
}
