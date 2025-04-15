package com.example.myactivityforresult;

import static android.os.Environment.getExternalStoragePublicDirectory;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // 1.permisions in manifest
    // 2.permisions in code
    // 3. add xml file to remember path for pictures (path.xml) for big photo
    // 4. provider in manifest - פותח גישה לנתונים משותפים, for big photo
    // 5. add launcher (go to camera and get the foto)

    Button btnSmall, btnBig, btnVideo, btnFromGallery, btnText;
    EditText et;
    ImageView iv;
    ActivityResultLauncher<Intent> arlSmall;
    ActivityResultLauncher<Uri> arlBig;
    ActivityResultLauncher<Intent> arlVideo;
    ActivityResultLauncher<String> arlFromGallery;
    ActivityResultLauncher<Intent> arlActivityToActivity;
    int count=0;
    Uri imageUri;// נדרש לצורך קבלת נתיב בו תהיה התמונה בגודל האמיתי בזיכרון המכשיר
    VideoView vivi;
    double myText=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAll();

        //2.permisions in code
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.CAMERA,
                             android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                             android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        // צילום תמונה מוקטנת לא נשמרת באופן אוטומטי בגלריה של הטלפון
        btnSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // יצירת אינטנט מרומז לשליחה לתוכנת מצלמה
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// get the photo
                arlSmall.launch(intent);
            }
        });

        // צילום תמונה בגודל מלא - נשמרת בזיכרון של הטלפון
        // בצילום תמונה בגודל אמיתי מתבצע צילום, שמירה בזיכרון ורק אז הצגה עג המסך
        btnBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    imageUri=createUri();// יצירת מיקום בזיכרון איפה שתשמר התמונה
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
                arlBig.launch(imageUri);// צילום התמונה
            }
        });

        // ביצוע ההקלטה של וידאו
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.setVisibility(View.GONE);
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                arlVideo.launch(takeVideoIntent);
            }
        });

        // הפעלת/ניגון רכיב הוידאו
        vivi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vivi.start();
            }
        });

        // קבלת תמונה מגלריית תמונות
        btnFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // פותח תיקיית מכשיר בה יש כל קבצי תמונה מכל הסוגים
                arlFromGallery.launch("image/*");
            }
        });

        // שליחת מספר אשר נקלט לאקטיביטי השני
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double myText=Double.parseDouble(et.getText().toString());
                Toast.makeText(MainActivity.this, ""+myText,
                                Toast.LENGTH_SHORT).show();
                Intent go=new Intent(MainActivity.this,
                                                    MainActivity2.class);
                go.putExtra("myKey",myText);
                arlActivityToActivity.launch(go);
            }
        });

    }

    private Uri createUri() throws IOException {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String currentDate = day + "_" + (month+1) + "_" + year +
                                "_" + hour + ":" + minute + ":" + second;
        //יצירת שם תמונה עם תאריך ניתן גם להוסיף מספר count
        String imageFileName = "MyAppImage_" + currentDate;
        //count++;
        //this line for real phone
        //File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //this line for emulator
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir     /* directory */
        );
        // אם מספר תמונה קיים יוצרים מספר חדש
        /*while (image.exists()) {
            image = new File(storageDir, imageFileName + "_" + count + ".jpg");
            count++;
        }*/
        return FileProvider.getUriForFile(getApplicationContext(),
                "com.example.myactivityforresult.fileProvider",
                image);
    }

    private void initAll() {
        btnSmall = findViewById(R.id.btnSmall);
        btnBig = findViewById(R.id.btnBig);
        btnVideo= findViewById(R.id.btnVideo);
        btnFromGallery= findViewById(R.id.btnGalery);
        btnText= findViewById(R.id.btnText);
        iv = findViewById(R.id.iv);
        vivi= findViewById(R.id.vivi);
        et = findViewById(R.id.et);

        iv.setVisibility(View.GONE);
        vivi.setVisibility(View.GONE);

        // קבלת תמונה מהמצלמה אם לחצו במצלמה "אוקי"
        arlSmall= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== RESULT_OK){
                            Intent data = result.getData();
                            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                            vivi.setVisibility(View.GONE);
                            iv.setVisibility(View.VISIBLE);
                            iv.setImageBitmap(bitmap);
                            /*Bundle bundle = result.getData().getExtras();// קבלת חבילת נתונים
                            Bitmap bitmap = (Bitmap) bundle.get("data");// מילה שמורה data
                            iv.setImageBitmap(bitmap);*/
                        }
                    }
                });

        // צילום תמונה בגודל האמיתי
        arlBig=registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        try {
                            if (result) {
                                vivi.setVisibility(View.GONE);
                                iv.setVisibility(View.VISIBLE);
                                iv.setImageURI(imageUri);
                            }
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this,
                                    "Photo Not found!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // צילום וידאו
        arlVideo=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                            Intent data = result.getData();
                            Uri videoUri = data.getData();
                            if (videoUri != null) {
                                vivi.setVisibility(View.VISIBLE);
                                vivi.setVideoURI(videoUri);
                                vivi.start();
                            }
                            else
                                Toast.makeText(MainActivity.this,
                                        "Video Not found !!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // קבלת תיקיית תמונות בחירת תמונה והצבתה בתוך iv
        arlFromGallery=registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result!=null)  {
                            Toast.makeText(MainActivity.this, "Gallery",
                                    Toast.LENGTH_SHORT).show();
                            vivi.setVisibility(View.GONE);
                            iv.setVisibility(View.VISIBLE);
                            iv.setImageURI(result);
                        }
                    }
                }
        );
        // פעולה אשר מחכה לתגובה מאקטיביטי השני לקבלת מספר לאחר השינוי
        arlActivityToActivity=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode()==RESULT_OK)  {
                            Intent takeresult=result.getData();
                            double res=takeresult.getDoubleExtra("result",0);
                            et.setText("New number: "+  res);
                        }
                    }
                });
    }
}