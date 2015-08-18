package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xamoom.android.mapping.ContentBlocks.ContentBlockType8;
import com.xamoom.android.xamoomcontentblocks.R;

/**
 * DownloadBlock
 */
public class ContentBlock8ViewHolder extends RecyclerView.ViewHolder {
    private Fragment mFragment;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private ImageView mIconImageView;
    private LinearLayout mRootLayout;

    public ContentBlock8ViewHolder(View itemView, Fragment fragment) {
        super(itemView);
        mRootLayout = (LinearLayout) itemView.findViewById(R.id.downloadBlockLinearLayout);
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mContentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
        mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
        mFragment = fragment;
    }

    public void setupContentBlock(final ContentBlockType8 cb8) {
        if(cb8.getTitle() != null)
            mTitleTextView.setText(cb8.getTitle());
        else
            mTitleTextView.setText(null);

        if(cb8.getText() != null)
            mContentTextView.setText(cb8.getText());
        else
            mContentTextView.setText(null);

        mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(cb8.getFileId()));
                mFragment.getActivity().startActivity(i);
            }
        });

        switch (cb8.getDownloadType()) {
            case 0:
                mRootLayout.setBackgroundResource(R.color.vcf_downloadBlock_background_color);
                mIconImageView.setImageResource(R.drawable.ic_account_plus);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            case 1:
                mRootLayout.setBackgroundResource(R.color.ical_downloadBlock_background_color);
                mIconImageView.setImageResource(R.drawable.ic_calendar);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            default:
                mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
                mIconImageView.setImageResource(R.drawable.ic_web);
                mTitleTextView.setTextColor(Color.parseColor("#333333"));
                mContentTextView.setTextColor(Color.parseColor("#333333"));
                break;
        }
    }
}
