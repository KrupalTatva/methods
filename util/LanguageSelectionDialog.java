

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageSelectionDialog extends Dialog {

    @BindView(R.id.ivView)
    ImageView ivView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.btnRetry)
    MaterialButton btnRetry;

    @BindView(R.id.btnSubmitFailed)
    MaterialButton btnSubmitFailed;

    @BindView(R.id.btnSubmit)
    MaterialButton btnSubmit;

    @BindView(R.id.llFailed)
    LinearLayout llFailed;

    private int view;
    private CharSequence title;
    private CharSequence positiveButton;
    private CharSequence negativeButton;
    private OnClickListener listener;

    public LanguageSelectionDialog(@NonNull Context context, @DrawableRes int view, CharSequence title, CharSequence positiveButton, CharSequence negativeButton, OnClickListener listener) {
        super(context);
        this.view = view;
        this.title = title;
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setContentView(R.layout.language_selection_dialog);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(R.color.transparent);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        ButterKnife.bind(this);

        tvTitle.setText(title);
        ivView.setImageResource(view);
        if (negativeButton.length() == 0) {
            llFailed.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
            btnSubmit.setText(positiveButton);
        } else {
            llFailed.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
            btnRetry.setText(negativeButton);
            btnSubmitFailed.setText(positiveButton);
        }
    }

    @OnClick({R.id.btnRetry, R.id.btnSubmitFailed, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRetry:
                dismiss();
                listener.onNegativeButtonClick();
                break;
            case R.id.btnSubmitFailed:
                dismiss();
                listener.onPositiveButtonClick();
                break;
            case R.id.btnSubmit:
                dismiss();
                listener.onPositiveButtonClick();
                break;
        }
    }

    public interface OnClickListener {
        void onPositiveButtonClick();
        void onNegativeButtonClick();
    }
}
