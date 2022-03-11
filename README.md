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



````
