# qrcodemodule模块使用

## 1.在工程的根目录build.gradle中添加jitpack库依赖

````

allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
	
````

## 2.在需要引用此类库模块的build.gradle中引入依赖

 ````
dependencies {
	implementation 'com.github.gb007:qrcodemodule:1.0.0'
	//权限申请，也可不添加，使用自己的权限申请代码
	implementation 'pub.devrel:easypermissions:1.0.1'
	}

````

## 3.初始化配置信息

### 3.1 Manifest中添加ScanCodeActivity扫描页面和GenerateCodeActivity生成二维码页面

````   

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.QRCodeApplication">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity android:name="com.hollysmart.zxingqrcodemodule.ScanCodeActivity" />

        <activity
            android:name="com.hollysmart.zxingqrcodemodule.GenerateCodeActivity"
            android:screenOrientation="portrait" />



    </application>

```` 

### 3.2 需要扫码或者生成二维码（条形码）的页面，申请权限（摄像头，读取文件，配置参数，跳转页面，接收识别结果

````

    //跳转扫码识别二维码（条形码）页面
    startActivityForResult(Intent(this, ScanCodeActivity::class.java),REQUEST_CODE_SACN_QRCODE)
          
    //接收二维码（条形码）扫描结果
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SACN_QRCODE) {
            var result = data?.getStringExtra("scanResult")
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show()
        }
    }

````

````
                //跳转生成二维码（条形码）页面
                var intent = Intent()
                intent.putExtra("code_type", 0)//0为不带logo ，1为中间带logo图案 ，2为条形码
                intent.putExtra("code_content", "111233233323")//二维码内容
             // intent.putExtra("code_logo",R.drawable.ic_launcher_background)//logo图案
                startActivity(Intent(this, GenerateCodeActivity::class.java))

````

### 3.3范例代码

````
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


````
