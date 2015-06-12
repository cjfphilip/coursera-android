package com.example.brucedelliott.modernartui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.graphics.Paint;
import android.graphics.Color;
import android.widget.SeekBar;


public class MainActivity extends Activity implements SurfaceHolder.Callback{

    private static final String TAG = "Lab-ModernArtUI";

    private DialogFragment mDialog;

    private ActionBar mActionBar;
    private SurfaceView mSurfaceView;
    private SeekBar mSeekBar;
    final private int mBackGroundColor = 0xffff8000;
    final private ColorDrawable mDrawableBackGroundColor = new ColorDrawable(mBackGroundColor);

    private Point2d myShape1 [];
    private Point2d myShape2 [];
    private Point2d myShape3 [];
    private Point2d myShape4 [];
    private Point2d myShape5 [];
    private Point2d myShape6 [];
    private Point2d myShape7 [];
    private Point2d myShape8 [];

    private int mShape1Color;
    private int mShape2Color;
    private int mShape3Color;
    private int mShape4Color;
    private int mShape5Color;
    private int mShape6Color;
    private int mShape7Color;
    private int mShape8Color;

    private float[] mShape1HSV;
    private float[] mShape2HSV;
    private float[] mShape3HSV;
    private float[] mShape4HSV;
    private float[] mShape5HSV;
    private float[] mShape6HSV;
    private float[] mShape7HSV;
    private float[] mShape8HSV;

    private float mShape1InitialH;
    private float mShape2InitialH;
    private float mShape3InitialH;
    private float mShape4InitialH;
    private float mShape5InitialH;
    private float mShape6InitialH;
    private float mShape7InitialH;
    private float mShape8InitialH;

    private class Point2d
    {
        float x,y;
        Point2d( float _x, float _y )
        {
            x = _x;
            y = _y;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout mFrameLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_main, null );

        mDialog = new MoreInformationDialogFragment();

        mActionBar = getActionBar();
        mActionBar.setBackgroundDrawable(mDrawableBackGroundColor);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        //mSurfaceView.setBackgroundColor(mBackGroundColor);
        //mSurfaceView.setDrawingCacheBackgroundColor(mBackGroundColor);
        mSurfaceView.getHolder().addCallback(this);
        mSurfaceView.setWillNotDraw(false);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setBackgroundColor(mBackGroundColor);
        mSeekBar.setMax( 360 );
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float colorVal = (float)progress;
                mShape1HSV[0] = (float)(mShape1InitialH + colorVal)%360.0f;
                mShape2HSV[0] = (float)(mShape2InitialH + colorVal)%360.0f;
                mShape3HSV[0] = (float)(mShape3InitialH + colorVal)%360.0f;
                mShape4HSV[0] = (float)(mShape4InitialH + colorVal)%360.0f;
                mShape6HSV[0] = (float)(mShape6InitialH + colorVal)%360.0f;
                mShape7HSV[0] = (float)(mShape7InitialH + colorVal)%360.0f;

                mShape1Color = Color.HSVToColor( mShape1HSV );
                mShape2Color = Color.HSVToColor( mShape2HSV );
                mShape3Color = Color.HSVToColor( mShape3HSV );
                mShape4Color = Color.HSVToColor( mShape4HSV );
                mShape6Color = Color.HSVToColor( mShape6HSV );
                mShape7Color = Color.HSVToColor( mShape7HSV );

                Canvas canvas = mSurfaceView.getHolder().lockCanvas();
                if (canvas == null) {
                    Log.e(TAG, "Cannot draw onto the canvas as it's null");
                } else {
                    drawMyStuff(canvas);
                    mSurfaceView.getHolder().unlockCanvasAndPost(canvas);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mShape1HSV = new float[3];
        mShape2HSV = new float[3];
        mShape3HSV = new float[3];
        mShape4HSV = new float[3];
        mShape5HSV = new float[3];
        mShape6HSV = new float[3];
        mShape7HSV = new float[3];
        mShape8HSV = new float[3];

        Color.RGBToHSV(0xff, 0x00, 0x00, mShape1HSV);
        Color.RGBToHSV(0xff, 0xff, 0x00, mShape2HSV );
        Color.RGBToHSV(0x00, 0xff, 0x00, mShape3HSV);
        Color.RGBToHSV(0x00, 0xff, 0xff, mShape4HSV );
        Color.RGBToHSV(0xff, 0xff, 0xff, mShape5HSV);
        Color.RGBToHSV(0x00, 0x00, 0xff, mShape6HSV );
        Color.RGBToHSV(0xff, 0x00, 0xff, mShape7HSV);
        Color.RGBToHSV(0xff, 0xff, 0xff, mShape8HSV );

        mShape1InitialH = mShape1HSV[0];
        mShape2InitialH = mShape2HSV[0];
        mShape3InitialH = mShape3HSV[0];
        mShape4InitialH = mShape4HSV[0];
        mShape5InitialH = mShape5HSV[0];
        mShape6InitialH = mShape6HSV[0];
        mShape7InitialH = mShape7HSV[0];
        mShape8InitialH = mShape8HSV[0];

        mShape1Color = Color.HSVToColor( mShape1HSV );
        mShape2Color = Color.HSVToColor( mShape2HSV );
        mShape3Color = Color.HSVToColor( mShape3HSV );
        mShape4Color = Color.HSVToColor( mShape4HSV );
        mShape5Color = Color.HSVToColor( mShape5HSV );
        mShape6Color = Color.HSVToColor( mShape6HSV );
        mShape7Color = Color.HSVToColor( mShape7HSV );
        mShape8Color = Color.HSVToColor( mShape8HSV );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Create a new AlertDialogFragment
        //mDialog = MoreInformationDialogFragment.newInstance();
        // Show AlertDialogFragment
        mDialog.show(getFragmentManager(), "More Information");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        tryDrawing(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int frmt, int w, int h) {
        tryDrawing(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    private void tryDrawing(SurfaceHolder holder) {
        Log.i(TAG, "Trying to draw...");

        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            Log.e(TAG, "Cannot draw onto the canvas as it's null");
        } else {
            drawMyStuff(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawMyStuff(final Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        drawMyShape1(width, height, canvas);
        drawMyShape2(width, height, canvas);
        drawMyShape3(width, height, canvas);
        drawMyShape4(width, height, canvas);
        drawMyShape5(width, height, canvas);
        drawMyShape6(width, height, canvas);
        drawMyShape7(width, height, canvas);
        drawMyShape8(width, height, canvas);
    }

    private void drawMyShape1( int width, int height, final Canvas canvas )
    {
        myShape1 = new Point2d[9];
        myShape1[0] = new Point2d( width*0/14, height*0/19 );
        myShape1[1] = new Point2d( width*4/14, height*0/19 );
        myShape1[2] = new Point2d( width*4/14, height*4/19 );
        myShape1[3] = new Point2d( width*7/14, height*4/19 );
        myShape1[4] = new Point2d( width*7/14, height*11/19 );
        myShape1[5] = new Point2d( width*4/14, height*11/19 );
        myShape1[6] = new Point2d( width*4/14, height*7/19 );
        myShape1[7] = new Point2d( width*0/14, height*7/19 );
        myShape1[8] = new Point2d( width*0/14, height*0/19 );
        Paint paint = new Paint();
        paint.setColor(mShape1Color);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();

        path.moveTo(myShape1[0].x, myShape1[0].y);
        for (int i=1; i<myShape1.length; i++)
        {
            path.lineTo( myShape1[i].x, myShape1[i].y );
        }
        canvas.drawPath(path, paint);
    }

    private void drawMyShape2( int width, int height, final Canvas canvas )
    {
        myShape2 = new Point2d[7];
        myShape2[0] = new Point2d( width*6/14, height*0/19 );
        myShape2[1] = new Point2d( width*14/14, height*0/19 );
        myShape2[2] = new Point2d( width*14/14, height*6/19 );
        myShape2[3] = new Point2d( width*9/14, height*6/19 );
        myShape2[4] = new Point2d( width*9/14, height*3/19 );
        myShape2[5] = new Point2d( width*6/14, height*3/19 );
        myShape2[6] = new Point2d( width*6/14, height*0/19 );
        Paint paint = new Paint();
        paint.setColor(mShape2Color);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();

        path.moveTo(myShape2[0].x, myShape2[0].y);
        for (int i=1; i<myShape2.length; i++)
        {
            path.lineTo( myShape2[i].x, myShape2[i].y );
        }
        canvas.drawPath(path, paint);
    }

    private void drawMyShape3( int width, int height, final Canvas canvas )
    {
        myShape3 = new Point2d[5];
        myShape3[0] = new Point2d( width*0/14, height*8/19 );
        myShape3[1] = new Point2d( width*3/14, height*8/19 );
        myShape3[2] = new Point2d( width*3/14, height*11/19 );
        myShape3[3] = new Point2d( width*0/14, height*11/19 );
        myShape3[4] = new Point2d( width*0/14, height*8/19 );
        Paint paint = new Paint();
        paint.setColor(mShape3Color);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();

        path.moveTo(myShape3[0].x, myShape3[0].y);
        for (int i=1; i<myShape3.length; i++)
        {
            path.lineTo( myShape3[i].x, myShape3[i].y );
        }
        canvas.drawPath(path, paint);
    }

    private void drawMyShape4( int width, int height, final Canvas canvas )
    {
        myShape4 = new Point2d[5];
        myShape4[0] = new Point2d( width*8/14, height*7/19 );
        myShape4[1] = new Point2d( width*14/14, height*7/19 );
        myShape4[2] = new Point2d( width*14/14, height*11/19 );
        myShape4[3] = new Point2d( width*8/14, height*11/19 );
        myShape4[4] = new Point2d( width*8/14, height*7/19 );
        Paint paint = new Paint();
        paint.setColor(mShape4Color);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();

        path.moveTo(myShape4[0].x, myShape4[0].y);
        for (int i=1; i<myShape4.length; i++)
        {
            path.lineTo( myShape4[i].x, myShape4[i].y );
        }
        canvas.drawPath(path, paint);
    }

    private void drawMyShape5( int width, int height, final Canvas canvas )
    {
        myShape5 = new Point2d[4];
        myShape5[0] = new Point2d( width*0/14, height*12/19 );
        myShape5[1] = new Point2d( width*5/14, height*12/19 );
        myShape5[2] = new Point2d( width*0/14, height*14/19 );
        myShape5[3] = new Point2d( width*0/14, height*12/19 );
        Paint paint = new Paint();
        paint.setColor(mShape5Color);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();

        path.moveTo(myShape5[0].x, myShape5[0].y);
        for (int i=1; i<myShape5.length; i++)
        {
            path.lineTo( myShape5[i].x, myShape5[i].y );
        }
        canvas.drawPath(path, paint);
    }


    private void drawMyShape6( int width, int height, final Canvas canvas )
    {
        myShape6 = new Point2d[5];
        myShape6[0] = new Point2d( width*0/14, height*15/19 );
        myShape6[1] = new Point2d( width*7/14, height*12/19 );
        myShape6[2] = new Point2d( width*14/14, height*12/19 );
        myShape6[3] = new Point2d( width*0/14, height*19/19 );
        myShape6[4] = new Point2d( width*0/14, height*15/19 );
        Paint paint = new Paint();
        paint.setColor(mShape6Color);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();

        path.moveTo(myShape6[0].x, myShape6[0].y);
        for (int i=1; i<myShape6.length; i++)
        {
            path.lineTo( myShape6[i].x, myShape6[i].y );
        }
        canvas.drawPath(path, paint);
    }

    private void drawMyShape7( int width, int height, final Canvas canvas )
    {
        myShape6 = new Point2d[5];
        myShape6[0] = new Point2d( width*0/14, height*19/19 );
        myShape6[1] = new Point2d( width*14/14, height*12/19 );
        myShape6[2] = new Point2d( width*14/14, height*16/19 );
        myShape6[3] = new Point2d( width*7/14, height*19/19 );
        myShape6[4] = new Point2d( width*0/14, height*19/19 );
        Paint paint = new Paint();
        paint.setColor(mShape7Color);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();

        path.moveTo(myShape6[0].x, myShape6[0].y);
        for (int i=1; i<myShape6.length; i++)
        {
            path.lineTo( myShape6[i].x, myShape6[i].y );
        }
        canvas.drawPath(path, paint);
    }

    private void drawMyShape8( int width, int height, final Canvas canvas )
    {
        myShape8 = new Point2d[4];
        myShape8[0] = new Point2d( width*9/14, height*19/19 );
        myShape8[1] = new Point2d( width*14/14, height*17/19 );
        myShape8[2] = new Point2d( width*14/14, height*19/19 );
        myShape8[3] = new Point2d( width*9/14, height*19/19 );
        Paint paint = new Paint();
        paint.setColor(mShape8Color);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();

        path.moveTo(myShape8[0].x, myShape8[0].y);
        for (int i=1; i<myShape8.length; i++)
        {
            path.lineTo( myShape8[i].x, myShape8[i].y );
        }
        canvas.drawPath(path, paint);
    }


    // Class that creates the AlertDialog
    public class MoreInformationDialogFragment extends DialogFragment {

        // Build AlertDialog using AlertDialog.Builder
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage("Inspired by the works of artists\nsuch as\nPiet Mondrian and Ben Nicholson.\n\nClick Below to learn more!")

                            // User cannot dismiss dialog by hitting back button
                    .setCancelable(false)

                            // Set up No Button
                    .setNegativeButton("Not Now",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                }
                            })

                            // Set up Yes Button
                    .setPositiveButton("Visit MOMA",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    Intent baseIntent = new Intent( Intent.ACTION_VIEW, Uri.parse("http://www.moma.org/"));
                                    Intent chooserIntent = Intent.createChooser(baseIntent, "Choose Your Browser Man!");
                                    if (baseIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivity(chooserIntent);
                                    }
                                }
                            }).create();
        }
    }

}
