package com.photoselector.ui;

import android.os.Bundle;

import com.photoselector.domain.PhotoSelectorDomain;
import com.photoselector.model.PhotoModel;
import com.photoselector.util.CommonUtils;

import java.util.List;

/**
 * Created by Hsien_ on 2015/1/27.
 */
public class CameraPreviewActivity extends BasePhotoPreviewActivity implements PhotoSelectorActivity.OnLocalReccentListener {

    private PhotoSelectorDomain photoSelectorDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBtnConfirmVisible(true);

        photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());

        init(getIntent().getExtras());
    }

    protected void init(Bundle extras) {
        if (extras == null)
            return;

        if (extras.containsKey("photos")) { // 预览图片
            photos = (List<PhotoModel>) extras.getSerializable("photos");
            current = extras.getInt("position", 0);
            updatePercent();
            bindData();
        } else if (extras.containsKey("album")) { // 点击图片查看
            String albumName = extras.getString("album"); // 相册
            this.current = extras.getInt("position");
            if (!CommonUtils.isNull(albumName) && albumName.equals(PhotoSelectorActivity.RECCENT_PHOTO)) {
                photoSelectorDomain.getReccent(this);
            } else {
                photoSelectorDomain.getAlbum(albumName, this);
            }
        }
    }

    @Override
    public void onPhotoLoaded(List<PhotoModel> photos) {
        this.photos = photos;
        updatePercent();
        bindData(); // 更新界面
    }
}
