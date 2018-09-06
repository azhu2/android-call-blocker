package com.alexazhu.callblocker.layout;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexazhu.callblocker.R;
import com.alexazhu.callblocker.activity.ConfigurationActivity;
import com.alexazhu.callblocker.blockednumber.BlockedNumber;

import java.util.Collection;

public class BlockedNumberListAdapter extends RecyclerView.Adapter<BlockedNumberListAdapter.BlockedNumberViewHolder> {
    private final ConfigurationActivity parentActivity;
    private final SortedList<BlockedNumber> blockedNumbers;

    public static class BlockedNumberViewHolder extends RecyclerView.ViewHolder {
        private final TextView matchTypeView;
        private final TextView phoneNumberView;
        private final ImageView deleteButtonView;

        private boolean isExpanded = false;

        private BlockedNumberViewHolder(@NonNull final View itemView) {
            super(itemView);

            matchTypeView = itemView.findViewById(R.id.match_type);
            phoneNumberView = itemView.findViewById(R.id.phone_number);
            deleteButtonView = itemView.findViewById(R.id.delete_button);
        }
    }

    public BlockedNumberListAdapter(ConfigurationActivity configurationActivity) {
        this.parentActivity = configurationActivity;
        this.blockedNumbers = new SortedList<>(BlockedNumber.class, new SortedListAdapterCallback<BlockedNumber>(this) {
            @Override
            public int compare(BlockedNumber item1, BlockedNumber item2) {
                return item1.toFormattedString().compareTo(item2.toFormattedString());
            }

            @Override
            public boolean areContentsTheSame(BlockedNumber item1, BlockedNumber item2) {
                return item1.equals(item2);
            }

            @Override
            public boolean areItemsTheSame(BlockedNumber item1, BlockedNumber item2) {
                return item1.equals(item2);
            }
        });
    }

    public void add(BlockedNumber number) {
        blockedNumbers.add(number);
    }

    public void addAll(Collection<BlockedNumber> numbers) {
        blockedNumbers.addAll(numbers);
    }

    public boolean contains(BlockedNumber number) {
        return blockedNumbers.indexOf(number) != SortedList.INVALID_POSITION;
    }

    public boolean remove(BlockedNumber number) {
        return blockedNumbers.remove(number);
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
        viewHolder.phoneNumberView.setText(number.toFormattedString());
        viewHolder.deleteButtonView.setOnClickListener((view) -> parentActivity.removeNumber(number));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull final BlockedNumberViewHolder viewHolder) {
        viewHolder.itemView.setOnClickListener((view) -> {
            Animator animator = AnimatorInflater.loadAnimator(parentActivity, viewHolder.isExpanded ? R.animator.collapse_list_item : R.animator.expand_list_item);
            animator.setTarget(viewHolder.itemView.findViewById(R.id.list_item));
            animator.start();
            viewHolder.isExpanded = !viewHolder.isExpanded;

            parentActivity.getWindow().getDecorView().performClick();
        });
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull final BlockedNumberViewHolder viewHolder) {
        if (viewHolder.isExpanded) {
            Animator animator = AnimatorInflater.loadAnimator(parentActivity, R.animator.collapse_list_item);
            animator.setTarget(viewHolder.itemView.findViewById(R.id.list_item));
            animator.start();
            viewHolder.isExpanded = !viewHolder.isExpanded;
        }
    }

    @Override
    public int getItemCount() {
        return blockedNumbers.size();
    }
}
