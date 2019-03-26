
<p align="center">
    <a href="#" target="_blank">
        <img src="logo/kiyo.png" width="64" alt="Kiyo Permissions Library" />
    </a>
</p>



[![](https://jitpack.io/v/AlexJuca/Kiyo.svg)](https://jitpack.io/#AlexJuca/Kiyo)

Kiyo is a simple to use and light-weight android permissions Library for Android M (API 23) and above.

Download
--------

Download the latest version via Gradle:

**Step 1:** Include jitpack to your projects build.gradle file
```
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
```

**Step 2:** Add the Kiyo dependency to your build.gradle file

```
dependencies
{
	implementation 'com.github.AlexJuca:Kiyo:0.0.1'
}
```


How to use
----------

**Step 1:** Set up a KiyoListener to handle callbacks. 
```java

private KiyoListener kiyoListener = new KiyoListener() {
        @Override
        public void onPermissionAccepted(int response) {
            call();
        }

        @Override
        public void onPermissionDenied(int response) {

        }

        @Override
        public void onShouldShowRequestPermissionRationale() {

        }
    };
```

**Step 2:** Set up Kiyo and define the permission that you need using the ```withPermission()``` method. Also pass in the KiyoListener that you defined to the ```withListener()``` method. Then call ```verify()``` to start the verification process. 

```java
    Kiyo.with(this).withPermission(Manifest.permission.CALL_PHONE).withListener(kiyoListener).verify();
```

**Step 3:** Implement the ```onRequestPermissionsResult()``` method in your Activity and in it's body call ```Kiyo.with(this).withListener(kiyoListener).onRequestPermissionsResult(requestCode, permissions, grantResults);``` passing in the same arguments.


```java
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Kiyo.with(this).withListener(kiyoListener).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
```


More context
----------

```java
public class MainActivity extends AppCompatActivity {
    private KiyoListener kiyoListener = new KiyoListener() {
        @Override
        public void onPermissionAccepted(int response) {
            call();
        }

        @Override
        public void onPermissionDenied(int response) {

        }

        @Override
        public void onShouldShowRequestPermissionRationale() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button call = findViewById(R.id.button);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        Kiyo.with(this).withPermission(Manifest.permission.CALL_PHONE).withListener(kiyoListener).verify();
    }

    @SuppressLint("MissingPermission")
    public void call() {
        Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel: 994590923" + Uri.encode("#")));
        startActivity(call);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Kiyo.with(this).withListener(kiyoListener).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
```


## Version history
``` 0.0.1: check for single permission - 26/03/2019 ``` <br/>

License
--------

    Copyright Alexandre Antonio Juca <corextechnologies@gmail.com>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

