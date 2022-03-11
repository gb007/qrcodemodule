package com.hollysmart.qrcodeapplication

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.hollysmart.zxingqrcodemodule.GenerateCodeActivity
import com.hollysmart.zxingqrcodemodule.ScanCodeActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val REQUEST_CODE_QRCODE_PERMISSIONS = 1

    private var startCode = 1; //1为scan 2为generate
    private val REQUEST_CODE_SACN_QRCODE = 999;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun onClick(view: View) {
        when (view.id) {
            R.id.test_scan_qrcode -> {
                startCode = 1
                requestCodeQRCodePermissions()
            }
            R.id.test_generate_qrcode -> {
                startCode = 2
                requestCodeQRCodePermissions()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions!!, grantResults!!, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String?>?) {}

    override fun onPermissionsDenied(requestCode: Int, perms: List<String?>?) {}

    @AfterPermissionGranted(1)
    private fun requestCodeQRCodePermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!EasyPermissions.hasPermissions(this, *perms)) {
            EasyPermissions.requestPermissions(
                this,
                "扫描二维码需要打开相机和散光灯的权限",
                1,
                *perms
            )
        } else {

            if (startCode == 1) {
                startActivityForResult(Intent(this, ScanCodeActivity::class.java),REQUEST_CODE_SACN_QRCODE)
            } else if (startCode == 2) {

                var intent = Intent()
                intent.putExtra("code_type", 0)//0为不带logo ，1为中间带logo图案 ，2为条形码
                intent.putExtra("code_content", "111233233323")//二维码内容
//              intent.putExtra("code_logo",R.drawable.ic_launcher_background)//logo图案
                startActivity(Intent(this, GenerateCodeActivity::class.java))
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SACN_QRCODE) {
            var result = data?.getStringExtra("scanResult")
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show()
        }
    }

}