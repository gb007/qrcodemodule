package com.hollysmart.zxingqrcodemodule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class GenerateCodeActivity extends AppCompatActivity {

    private ImageView iv_encode;
    private TextView tv_code_type;

    private int QRCode_Type = 0;
    private int code_logo = 0;
    private String code_content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxing_qrcode_module_activity_generate);
        initView();
        initData();
        createQRCode();
    }

    private void initView() {
        iv_encode = findViewById(R.id.iv_encode);
        tv_code_type = findViewById(R.id.tv_code_type);
    }

    private void initData() {

        QRCode_Type = getIntent().getIntExtra("code_type", 0);
        code_content = getIntent().getStringExtra("code_content");

        if (QRCode_Type == 1) {
            code_logo = getIntent().getIntExtra("code_logo", 0);
        }

    }


    private void createQRCode() {

        if(QRCode_Type == 1){//中间带logo二维码
            createChineseQRCodeWithLogo();
        }else if(QRCode_Type == 2){//条形码
            createBarcodeWithoutContent();
        }else{//普通二维码
            createChineseQRCode();
        }

        createChineseQRCode();
//        createEnglishQRCode();
//        createChineseQRCodeWithLogo();
//        createEnglishQRCodeWithLogo();
//
//        createBarcodeWidthContent();
//        createBarcodeWithoutContent();
    }

    private void createChineseQRCode() {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
        .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(code_content, BGAQRCodeUtil.dp2px(GenerateCodeActivity.this, 150));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_encode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(GenerateCodeActivity.this, "生成中文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void createEnglishQRCode() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode("bingoogolapple", BGAQRCodeUtil.dp2px(GenerateCodeActivity.this, 150), Color.parseColor("#ff0000"));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_encode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(GenerateCodeActivity.this, "生成英文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void createChineseQRCodeWithLogo() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap logoBitmap = BitmapFactory.decodeResource(GenerateCodeActivity.this.getResources(), code_logo);
                return QRCodeEncoder.syncEncodeQRCode(code_content, BGAQRCodeUtil.dp2px(GenerateCodeActivity.this, 150), Color.parseColor("#ff0000"), logoBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_encode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(GenerateCodeActivity.this, "生成带logo的中文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void createEnglishQRCodeWithLogo() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap logoBitmap = BitmapFactory.decodeResource(GenerateCodeActivity.this.getResources(), R.mipmap.zxing_qrcode_module_logo);
                return QRCodeEncoder.syncEncodeQRCode("bingoogolapple", BGAQRCodeUtil.dp2px(GenerateCodeActivity.this, 150), Color.BLACK, Color.WHITE,
                        logoBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_encode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(GenerateCodeActivity.this, "生成带logo的英文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


    private void createBarcodeWidthContent() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                int width = BGAQRCodeUtil.dp2px(GenerateCodeActivity.this, 150);
                int height = BGAQRCodeUtil.dp2px(GenerateCodeActivity.this, 70);
                int textSize = BGAQRCodeUtil.sp2px(GenerateCodeActivity.this, 14);
                return QRCodeEncoder.syncEncodeBarcode("bga123", width, height, textSize);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_encode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(GenerateCodeActivity.this, "生成条底部带文字形码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void createBarcodeWithoutContent() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                int width = BGAQRCodeUtil.dp2px(GenerateCodeActivity.this, 150);
                int height = BGAQRCodeUtil.dp2px(GenerateCodeActivity.this, 70);
                return QRCodeEncoder.syncEncodeBarcode(code_content, width, height, 0);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    iv_encode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(GenerateCodeActivity.this, "生成条底部不带文字形码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

//    public void decodeChinese(View v) {
//        mChineseIv.setDrawingCacheEnabled(true);
//        Bitmap bitmap = mChineseIv.getDrawingCache();
//        decode(bitmap, "解析中文二维码失败");
//    }
//
//    public void decodeEnglish(View v) {
//        mEnglishIv.setDrawingCacheEnabled(true);
//        Bitmap bitmap = mEnglishIv.getDrawingCache();
//        decode(bitmap, "解析英文二维码失败");
//    }
//
//    public void decodeChineseWithLogo(View v) {
//        mChineseLogoIv.setDrawingCacheEnabled(true);
//        Bitmap bitmap = mChineseLogoIv.getDrawingCache();
//        decode(bitmap, "解析带logo的中文二维码失败");
//    }
//
//    public void decodeEnglishWithLogo(View v) {
//        mEnglishLogoIv.setDrawingCacheEnabled(true);
//        Bitmap bitmap = mEnglishLogoIv.getDrawingCache();
//        decode(bitmap, "解析带logo的英文二维码失败");
//    }
//
//    public void decodeBarcodeWithContent(View v) {
//        mBarcodeWithContentIv.setDrawingCacheEnabled(true);
//        Bitmap bitmap = mBarcodeWithContentIv.getDrawingCache();
//        decode(bitmap, "识别底部带文字的条形码失败");
//    }
//
//    public void decodeBarcodeWithoutContent(View v) {
//        mBarcodeWithoutContentIv.setDrawingCacheEnabled(true);
//        Bitmap bitmap = mBarcodeWithoutContentIv.getDrawingCache();
//        decode(bitmap, "识别底部没带文字的条形码失败");
//    }

    private void decode(final Bitmap bitmap, final String errorTip) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
        .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return QRCodeDecoder.syncDecodeQRCode(bitmap);
            }

            @Override
            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(GenerateCodeActivity.this, errorTip, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GenerateCodeActivity.this, result, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}