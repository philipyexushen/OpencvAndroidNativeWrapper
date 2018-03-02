package philipyexushen.opencvhandler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.*;

enum ImgState{
    Original, WithDetect
}

public class MainActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    static {
        System.loadLibrary("opencvhandler");
    }

    private ImageView imageView;
    private Button btn1, btn2;
    private Bitmap bitmapOriginal, bitmapResult;
    private Bitmap bitmapTempl;
    private SeekBar cannyThresholdSeekBar, accumulatorThresholdSeekBar;
    private EditText editText1, editText2;
    private ImgState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        imageView = findViewById(R.id.imageView);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn1.setEnabled(false);
        btn2.setEnabled(true);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        cannyThresholdSeekBar = findViewById(R.id.cannyThresholdSeekBar);
        accumulatorThresholdSeekBar = findViewById(R.id.accumulatorThresholdSeekBar);

        cannyThresholdSeekBar.setOnSeekBarChangeListener(this);
        accumulatorThresholdSeekBar.setOnSeekBarChangeListener(this);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);

        int v1 = cannyThresholdSeekBar.getProgress();
        int v2 = accumulatorThresholdSeekBar.getProgress();
        editText1.setText(String.valueOf(v1), TextView.BufferType.NORMAL);
        editText2.setText(String.valueOf(v2), TextView.BufferType.NORMAL);

        editText1.setKeyListener(null);
        editText2.setKeyListener(null);
        editText1.setFocusable(false);
        editText2.setFocusable(false);

        bitmapOriginal = ((BitmapDrawable)getResources().getDrawable(R.drawable.coinsrot2)).getBitmap();
        //bitmapOriginal = ((BitmapDrawable)getResources().getDrawable(R.drawable.pic1)).getBitmap();
        bitmapTempl = ((BitmapDrawable)getResources().getDrawable(R.drawable.templ2)).getBitmap();

        //bitmapResult = HandlerWrapper.houghCircles(bitmapOriginal,
         //       1, h/8, 200,48,0,0);
        float[] position = HandlerWrapper.generalizedHoughBallard(bitmapOriginal, bitmapTempl,
                50,100, v1,2, 360, v2, 1000);
        /*
                float[] position = HandlerWrapper.generalizedHoughGuil(bitmapOriginal, bitmapTempl,
                50,100, v1,2,360, v2,
                1,1.1,0.1,100,
                0,1,1,1000,
                1000);
         */


        int tempH = bitmapTempl.getHeight(), tempW = bitmapTempl.getWidth();
        bitmapResult = HandlerWrapper.drawGeneralizedHough(bitmapOriginal, position,
                tempH, tempW, 0,0,255,1,8,0);
        imageView.setImageBitmap(bitmapOriginal);

        state = ImgState.Original;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                btn1.setEnabled(false);
                btn2.setEnabled(true);

                imageView.setImageBitmap(bitmapOriginal);

                state = ImgState.Original;
                break;
            case R.id.button2:
                btn1.setEnabled(true);
                btn2.setEnabled(false);

                state = ImgState.WithDetect;
                imageView.setImageBitmap(bitmapResult);
            default:

        }
    }

    public void onProgressChanged(SeekBar seekBar, int value, boolean var3){
        int v1 = cannyThresholdSeekBar.getProgress();
        int v2 = accumulatorThresholdSeekBar.getProgress();
        int h = bitmapOriginal.getHeight();

        switch (seekBar.getId()){
            case R.id.cannyThresholdSeekBar:
                editText1.setText(String.valueOf(v1), TextView.BufferType.NORMAL);
                break;
            case R.id.accumulatorThresholdSeekBar:
                editText2.setText(String.valueOf(v2), TextView.BufferType.NORMAL);
                break;
            default:
        }
        //bitmapResult = HandlerWrapper.houghCircles(bitmapOriginal, 1, h/8, v1, v2, 0,0);
        float[] position = HandlerWrapper.generalizedHoughBallard(bitmapOriginal, bitmapTempl,
                50,100,v1,2, 360, v2, 1000);
        /*
                float[] position = HandlerWrapper.generalizedHoughGuil(bitmapOriginal, bitmapTempl,
                50,100, v1,2,360, v2,
                1,1.1,0.1,100,
                0,1,1,1000,
                1000);
         */

        int tempH = bitmapTempl.getHeight(), tempW = bitmapTempl.getWidth();
        bitmapResult= HandlerWrapper.drawGeneralizedHough(bitmapOriginal, position,
                tempH, tempW, 0,0,255,1,8,0);

        if (state == ImgState.WithDetect){
            imageView.setImageBitmap(bitmapResult);
        }
    }

    public void onStartTrackingTouch(SeekBar var1){

    }

    public void onStopTrackingTouch(SeekBar var1){

    }
}
