package philipyexushen.opencvhandler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

enum ImgState{
    Original, WithDetect
}

public class MainActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("opencvhandler");
    }

    private ImageView imgaeView;
    private Button btn1, btn2;
    private Bitmap bitmapOriginal, bitmapWithHoughCircles;
    private SeekBar cannyThresholdSeekBar, accumulatorThresholdSeekBar;
    private EditText editText1, editText2;
    private ImgState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        imgaeView = (ImageView)findViewById(R.id.imageView);
        btn1 = (Button)findViewById(R.id.button1);
        btn2 = (Button)findViewById(R.id.button2);
        btn1.setEnabled(false);
        btn2.setEnabled(true);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        cannyThresholdSeekBar = (SeekBar)findViewById(R.id.cannyThresholdSeekBar);
        accumulatorThresholdSeekBar = (SeekBar)findViewById(R.id.accumulatorThresholdSeekBar);

        cannyThresholdSeekBar.setOnSeekBarChangeListener(this);
        accumulatorThresholdSeekBar.setOnSeekBarChangeListener(this);

        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);

        editText1.setText(String.valueOf(cannyThresholdSeekBar.getProgress()), TextView.BufferType.NORMAL);
        editText2.setText(String.valueOf(accumulatorThresholdSeekBar.getProgress()), TextView.BufferType.NORMAL);

        editText1.setKeyListener(null);
        editText2.setKeyListener(null);
        editText1.setFocusable(false);
        editText2.setFocusable(false);

        bitmapOriginal = ((BitmapDrawable)getResources().getDrawable(R.drawable.coinsrot2)).getBitmap();
        imgaeView.setImageBitmap(bitmapOriginal);

        int h = bitmapOriginal.getHeight();
        bitmapWithHoughCircles = HandlerWrapper.houghCircles(bitmapOriginal, 1, h/8, 200,48,0,0);

        state = ImgState.Original;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                btn1.setEnabled(false);
                btn2.setEnabled(true);

                imgaeView.setImageBitmap(bitmapOriginal);

                state = ImgState.Original;
                break;
            case R.id.button2:
                btn1.setEnabled(true);
                btn2.setEnabled(false);

                state = ImgState.WithDetect;
                imgaeView.setImageBitmap(bitmapWithHoughCircles);
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
        bitmapWithHoughCircles = HandlerWrapper.houghCircles(bitmapOriginal, 1, h/8, v1, v2, 0,0);

        if (state == ImgState.WithDetect){
            imgaeView.setImageBitmap(bitmapWithHoughCircles);
        }
    }

    public void onStartTrackingTouch(SeekBar var1){

    }

    public void onStopTrackingTouch(SeekBar var1){

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
