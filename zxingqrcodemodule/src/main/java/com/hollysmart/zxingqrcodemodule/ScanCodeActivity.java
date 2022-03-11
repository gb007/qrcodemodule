package com.hollysmart.zxingqrcodemodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;

import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanCodeActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final String TAG = ScanCodeActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 100;
    private static final int RESULT_CODE_SCAN = 999;

    private ZXingView mZXingView;
    private ImageView img_flash_light;

    private boolean isOPenedFlashLight = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxing_qrcode_module_activity_scan);
        mZXingView = findViewById(R.id.zxingview);
        img_flash_light= findViewById(R.id.img_flash_light);
        mZXingView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        setTitle("扫描结果为：" + result);
        vibrate();
        Intent intent = new Intent();
        intent.putExtra( "scanResult", result );
        setResult(RESULT_CODE_SCAN, intent);
        finish();

//        mZXingView.startSpot(); // 开始识别
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_flash_light) {
            if (isOPenedFlashLight) {
                img_flash_light.setBackgroundResource(R.drawable.zxing_qrcode_module_icon_flash_light);
                mZXingView.closeFlashlight(); // 关闭闪光灯
                isOPenedFlashLight = false;
            } else {
                img_flash_light.setBackgroundResource(R.drawable.zxing_qrcode_module_icon_flash_light_opened);
                mZXingView.openFlashlight(); // 打开闪光灯
                isOPenedFlashLight = true;
            }
        } else if (id == R.id.img_album) {

            new ImagePicker()
                    .pickType(ImagePickType.SINGLE)
                    .maxNum(1)
                    .needCamera(false)
//                    .cachePath(getCacheDir().getPath())
//                    .doCrop(new ImagePickerCropParams())
                    .start(this, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            List<ImageBean> picPaths = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
            String picturePath = picPaths.get(0).getImagePath();
//            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
//            // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
            mZXingView.decodeQRCode(picturePath);

            /*
            没有用到 QRCodeView 时可以调用 QRCodeDecoder 的 syncDecodeQRCode 方法

            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
            .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
//            new AsyncTask<Void, Void, String>() {
//                @Override
//                protected String doInBackground(Void... params) {
//                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
//                }
//
//                @Override
//                protected void onPostExecute(String result) {
//                    if (TextUtils.isEmpty(result)) {
//                        Toast.makeText(TestScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }.execute();
        }
    }


}