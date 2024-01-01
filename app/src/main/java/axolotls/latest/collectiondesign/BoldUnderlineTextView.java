package axolotls.latest.collectiondesign;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class BoldUnderlineTextView extends AppCompatTextView {

    private boolean isBold = false;
    private boolean isUnderlineVisible = false;

    public BoldUnderlineTextView(Context context) {
        super(context);
    }

    public BoldUnderlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoldUnderlineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isBold && isUnderlineVisible) {
            Paint paint = getPaint();
            paint.setUnderlineText(false); // Disable underline so that we can draw a custom line
            paint.setTypeface(Typeface.create(getTypeface(), Typeface.NORMAL));

            int underlineHeight = 7; // Set your desired thickness
            int paddingBetweenTextAndUnderline = -12; // Set your desired space

            int y = getHeight() - getPaddingBottom() - paddingBetweenTextAndUnderline;
            canvas.drawRect(getPaddingLeft(), y - underlineHeight, getWidth() - getPaddingRight(), y, paint);
        }
    }

    public void setBold(boolean bold) {
        isBold = bold;
        invalidate();
    }

    public void setUnderlineVisible(boolean underlineVisible) {
        isUnderlineVisible = underlineVisible;
        invalidate();
    }
}
