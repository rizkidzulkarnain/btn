package com.mitkoindo.smartcollection.utilities;

/**
 * Created by W8 on 28/09/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mitkoindo.smartcollection.R;

public class DiagonalLineView extends View
{
    private int dividerColor;
    private Paint paint;

    public DiagonalLineView(Context context)
    {
        super(context);
        init(context);
    }

    public DiagonalLineView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public DiagonalLineView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context)
    {
        dividerColor = ContextCompat.getColor(context, R.color.White);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(dividerColor);
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawLine(0, 0, getWidth(), getHeight(), paint);
    }

}
