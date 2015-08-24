package tickets.language.svp.languagetickets.ui.controls;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Pasha on 3/7/2015.
 */
public class SingleLineTextView extends TextView {

    public SingleLineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
    }

    public SingleLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
    }

    public SingleLineTextView(Context context) {
        super(context);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final Layout layout = getLayout();
        if (layout != null) {
            final int lineCount = layout.getLineCount();
            if (lineCount > 0) {
                final int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
                if (ellipsisCount > 0) {

                    final float textSize = getTextSize();

                    // textSize is already expressed in pixels
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize - 1));

                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            }
        }
    }
    public void resize(String text, float textViewWidth, float textViewHeight) {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(1);
        p.getTextBounds(text, 0, text.length(), bounds);
        float widthDifference = (textViewWidth)/bounds.width();
        float heightDifference = (textViewHeight);
        float textSize = Math.min(widthDifference, heightDifference);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

}