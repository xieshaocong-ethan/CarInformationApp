package ethan.demo.library;

import android.content.res.Resources;

class Utils {

    static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }
}
