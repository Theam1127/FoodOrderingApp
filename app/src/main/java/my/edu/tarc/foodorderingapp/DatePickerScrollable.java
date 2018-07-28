package my.edu.tarc.foodorderingapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

public class DatePickerScrollable extends DatePicker {

    public DatePickerScrollable(Context context) {
        super(context);
    }

    public DatePickerScrollable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DatePickerScrollable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }
}
