package au.com.traveller.timeblocks;

import android.content.Context;

public class UiUtil
{
    public static int dipToPixels(Context context, int valueInDIPs)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        final int valueInPixels = (int) (valueInDIPs * scale);

        return valueInPixels;
    }

}
