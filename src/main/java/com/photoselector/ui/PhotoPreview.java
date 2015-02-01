package com.photoselector.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.photoselector.R;
import com.photoselector.model.PhotoModel;
import com.polites.GestureImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class PhotoPreview extends LinearLayout implements OnClickListener {

	private ProgressBar pbLoading;
	private GestureImageView ivContent;
	private OnClickListener l;
    private Context mContext ;

	public PhotoPreview(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.view_photopreview, this, true);

		pbLoading = (ProgressBar) findViewById(R.id.pb_loading_vpp);
		ivContent = (GestureImageView) findViewById(R.id.iv_content_vpp);
		ivContent.setOnClickListener(this);

        mContext = context;
	}

	public PhotoPreview(Context context, AttributeSet attrs, int defStyle) {
		this(context);
	}

	public PhotoPreview(Context context, AttributeSet attrs) {
		this(context);
	}

	public void loadImage(PhotoModel photoModel) {
		loadImage("file://" + photoModel.getOriginalPath());
	}

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            ivContent.setImageBitmap(bitmap);
            pbLoading.setVisibility(View.GONE);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            ivContent.setImageDrawable(getResources().getDrawable(R.drawable.ic_picture_loadfailed));
            pbLoading.setVisibility(View.GONE);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

	private void loadImage(String path) {

//        Picasso.with(mContext).load(path).fit().centerInside().error(R.drawable.ic_picture_loadfailed).into(ivContent, new Callback() {
//            @Override
//            public void onSuccess() {
//                pbLoading.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onError() {
//                pbLoading.setVisibility(View.GONE);
//            }
//        });
        WindowManager wm = (WindowManager)mContext.getSystemService(mContext.WINDOW_SERVICE);
        android.view.Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        Picasso.with(mContext).load(path).resize(metrics.widthPixels,metrics.heightPixels).centerInside().into(target);
		/*ImageLoader.getInstance().loadImage(path, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				ivContent.setImageBitmap(loadedImage);
				pbLoading.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				ivContent.setImageDrawable(getResources().getDrawable(R.drawable.ic_picture_loadfailed));
				pbLoading.setVisibility(View.GONE);
			}
		});*/
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.l = l;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_content_vpp && l != null)
			l.onClick(ivContent);
	}

    @Override
    protected void onDetachedFromWindow() {
        Picasso.with(mContext).cancelRequest(ivContent);
        super.onDetachedFromWindow();
    }
}
