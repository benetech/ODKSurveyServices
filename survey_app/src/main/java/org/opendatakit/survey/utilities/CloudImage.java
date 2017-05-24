package org.opendatakit.survey.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.opendatakit.survey.R;

/**
 * Created by jakub on 24.05.17.
 */

public class CloudImage extends View {

    private Paint mPaint = new Paint();

    public CloudImage(Context context) {
        super (context);
    }
    public CloudImage(Context context, AttributeSet attrs) {
        super (context, attrs);
    }

    public void onDraw (Canvas canvas) {
        float midX, midY, radius;
        midX = getWidth() / 2;
        midY = getHeight() / 2;
        if (midX < midY) {
            radius = midX / 2;
        } else {
            radius = midY / 2;
        }
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(midX, midY, radius, mPaint);
        Bitmap cloud = BitmapFactory.decodeResource(getResources(), R.drawable.cloud, null);
        canvas.drawBitmap(cloud, midX / 3 + 5, midY / 3 + 5, mPaint);
    }

}
