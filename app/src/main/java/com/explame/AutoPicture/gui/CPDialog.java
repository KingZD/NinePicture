package com.explame.AutoPicture.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.explame.AutoPicture.R;

/**
 * Created by zd on 2017/2/27.
 */

public class CPDialog extends Dialog{
    private TextView tvTitle;
    private TextView tvTag;
    private TextView tvMessage;
    private TextView tvPositiveButton;
    private TextView tvNeutralButton;
    private TextView tvNegativeButton;

    private String mPositiveButtonText, mNeutralButtonText, mNegativeButtonText;

    private View.OnClickListener mPositiveButtonClickListener, mNeutralButtonClickListener, mNegativeButtonClickListener;

    private ButtonType mDefaultButtonType = ButtonType.Positive;
    public  enum ButtonType{
        Positive,Neutral,Negative
    }

    @Override
    public void setTitle(int resId) {
        super.setTitle(resId);
        tvTitle.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        tvTitle.setText(title);
    }

    public CPDialog setTag(int resId) {
        tvTag.setText(resId);
        return this;
    }

    public CPDialog setTag(String message) {
        tvTag.setVisibility(View.VISIBLE);
        tvTag.setText(message);
        return this;
    }

    public CPDialog setMessage(int resId) {
        tvMessage.setText(resId);
        return this;
    }

    public CPDialog setMessage(String message) {
        tvMessage.setText(message);
        return this;
    }

    public CPDialog setPositiveButton(String text, View.OnClickListener clickListener) {
        mPositiveButtonText = text;
        mPositiveButtonClickListener = clickListener;
        return this;
    }

    public CPDialog setNeutralButton(String text, View.OnClickListener clickListener) {
        mNeutralButtonText = text;
        mNeutralButtonClickListener = clickListener;
        return this;
    }

    public CPDialog setNegativeButton(String text, View.OnClickListener clickListener) {
        mNegativeButtonText = text;
        mNegativeButtonClickListener = clickListener;
        return this;
    }

    public CPDialog(Context context) {
        super(context);
        initDialog();
    }

    protected CPDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog();
    }

    protected CPDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    public void initDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTag = (TextView) findViewById(R.id.tvTag);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvPositiveButton = (TextView) findViewById(R.id.tvPositiveButton);
        tvNeutralButton = (TextView) findViewById(R.id.tvNeutralButton);
        tvNegativeButton = (TextView) findViewById(R.id.tvNegativeButton);
    }

    @Override
    public void show() {
        if (!TextUtils.isEmpty(mPositiveButtonText)) {
            tvPositiveButton.setText(mPositiveButtonText);
            tvPositiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPositiveButtonClickListener != null)
                        mPositiveButtonClickListener.onClick(view);
                    dismiss();
                }
            });
        } else {
            tvPositiveButton.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mNeutralButtonText)) {
            tvNeutralButton.setText(mNeutralButtonText);
            tvNeutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mNeutralButtonClickListener != null)
                        mNeutralButtonClickListener.onClick(view);
                    dismiss();
                }
            });
        } else {
            tvNeutralButton.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mNegativeButtonText)) {
            tvNegativeButton.setText(mNegativeButtonText);
            tvNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mNegativeButtonClickListener != null)
                        mNegativeButtonClickListener.onClick(view);
                    dismiss();
                }
            });
        } else {
            tvNegativeButton.setVisibility(View.GONE);
        }


        switch (mDefaultButtonType){
            case Positive:
                tvPositiveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.color_3a3a3a));
                tvPositiveButton.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            case Neutral:
                tvNeutralButton.setTextColor(ContextCompat.getColor(getContext(),R.color.color_3a3a3a));
                tvNeutralButton.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            case Negative:
                tvNegativeButton.setTextColor(ContextCompat.getColor(getContext(),R.color.color_3a3a3a));
                tvNegativeButton.setTypeface(Typeface.DEFAULT_BOLD);
                break;
        }

        super.show();
    }
}
