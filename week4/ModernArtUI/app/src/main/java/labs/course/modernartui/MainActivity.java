package labs.course.modernartui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.lang.reflect.Field;
import java.util.Random;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class MainActivity extends ActionBarActivity {

    Random r = new Random();
    private final String TAG = this.getClass().getSimpleName();
    LinearLayout mMainLayout;
    LinearLayout mBlackLayout;
    SeekBar mSeekBar;
    int maxAmount = 255;
    int lastSeekPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeActionOverflowMenuShown();

        mMainLayout = (LinearLayout) findViewById(R.id.ll_container);

        mSeekBar = (SeekBar) findViewById(R.id.sb_change_colours);
        mSeekBar.setMax(maxAmount);
        mSeekBar.setProgress(maxAmount / 2);
        lastSeekPosition = mSeekBar.getProgress();

        generateRandomLayout();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustColours(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void generateRandomLayout() {
        //Randomly create some linear layouts that have a random colour.
        mMainLayout.removeAllViews();
        int margin = 3;
        int weight = 1;
        int totalVLayouts = randomBlocks();
        int greyVPos = r.nextInt(totalVLayouts) + 1;

        for (int i = 1; i <= totalVLayouts; i++) {
            weight = randomWeight();
            margin = 1;
            LinearLayout randomVLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutVParams = new LinearLayout.LayoutParams(MATCH_PARENT, 0, weight);
            randomVLayout.setLayoutParams(layoutVParams);
            randomVLayout.setOrientation(LinearLayout.HORIZONTAL);
            int totalHLayouts = randomBlocks();
            int greyHPos = r.nextInt(totalHLayouts) + 1;
            Log.d(TAG, "> " + i + "/" + totalVLayouts + " - " + weight + ")");
            for (int j = 1; j <= totalHLayouts; j++) {
                LinearLayout randomHLayout = new LinearLayout(this);
                int r = randomColour();
                int g = randomColour();
                int b = randomColour();
                weight = randomWeight();
                margin = 1;
                if (i == greyVPos && j == greyHPos) {
                    mBlackLayout = randomHLayout;
                    r = 0;
                    g = 0;
                    b = 0;
                    margin = 10;
                }
                LinearLayout.LayoutParams layoutHParams = new LinearLayout.LayoutParams(0, MATCH_PARENT, weight);
                layoutHParams.setMargins(margin, margin, margin, margin);
                randomHLayout.setLayoutParams(layoutHParams);
                randomHLayout.setBackgroundColor(Color.rgb(r, g, b));
                randomVLayout.addView(randomHLayout);
                Log.d(TAG, ">>> H " + i + "/" + totalHLayouts + " - " + weight + ")");
            }

            mMainLayout.addView(randomVLayout);
        }
        mMainLayout.invalidate();
        mMainLayout.requestLayout();
        adjustColours(mSeekBar.getProgress());
    }

    private void adjustColours(int progress) {
        int amount = progress - lastSeekPosition;
        lastSeekPosition = progress;
        for (int i = 0; i < mMainLayout.getChildCount(); i++) {
            LinearLayout vLayout = (LinearLayout) mMainLayout.getChildAt(i);
            for (int j = 0; j < vLayout.getChildCount(); j++) {
                LinearLayout hLayout = (LinearLayout) vLayout.getChildAt(j);
                if (hLayout != mBlackLayout) {
                    ColorDrawable layoutColourDrawable = (ColorDrawable) hLayout.getBackground();
                    int layoutColour = layoutColourDrawable.getColor();
                    int r = (layoutColour >> 16) & 0xFF;
                    int g = (layoutColour >> 8) & 0xFF;
                    int b = (layoutColour >> 0) & 0xFF;
                    r += amount;
                    g += amount;
                    b += amount;
                    if (r > 255) r = r - 255;
                    if (g > 255) g = g - 255;
                    if (b > 255) b = b - 255;
                    if (r < 0) r = r + 255;
                    if (g < 0) g = g + 255;
                    if (b < 0) b = b + 255;
                    hLayout.setBackgroundColor(Color.rgb(r, g, b));
                }
            }
        }
    }

    private int randomBlocks() {
        return r.nextInt(3) + 3;
    }

    private int randomWeight() {
        return r.nextInt(10) + 1;
    }

    private int randomColour() {
        return r.nextInt(256);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.more_information) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.visit_moma_title)
                    .setMessage(getString(R.string.visit_moma_message))
                    .setPositiveButton(R.string.visit_moma, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String url = "http://www.moma.org/";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    })
                    .setNegativeButton(R.string.notnow, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if (id == R.id.random) {
            generateRandomLayout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }
}
