package com.example.myactivityforresult;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    RadioButton rbMul,rbDiv,rbPow,rbSqrt;
    Button btnCalc;
    double num;
    TextView tv;

    Intent takeit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        init();

        takeit=getIntent();
        num=takeit.getDoubleExtra("myKey",0);
        tv.setText("Old num: "+num);

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calc();
                Intent data = new Intent();
                data.putExtra("result", num);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    public void init(){
            rbDiv = findViewById(R.id.rbDiv);
            rbMul = findViewById(R.id.rbMul);
            rbPow = findViewById(R.id.rbPow);
            rbSqrt = findViewById(R.id.rbSqrt);
            btnCalc = findViewById(R.id.btnCalc);
            tv=findViewById(R.id.tv);
    }
    public void calc(){
        if(rbMul.isChecked()){
            num*=2;
        }
        if(rbDiv.isChecked()){
            num/=2;
        }
        if(rbPow.isChecked()){
            num = Math.pow(num,2);
        }
        if(rbSqrt.isChecked()){
            num = Math.sqrt(num);
        }
    }
}