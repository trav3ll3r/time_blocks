package au.com.traveller.timeblocks;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UiUtil
{
    private static SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private static SimpleDateFormat FORMAT_WEEKDAY = new SimpleDateFormat("EEEE", Locale.getDefault());

    public static int dipToPixels(Context context, int valueInDIPs)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        final int valueInPixels = (int) (valueInDIPs * scale);

        return valueInPixels;
    }

    public static String date2string(Calendar forDate)
    {
        if (forDate == null)
        {
            return "null";
        }
        else
        {
            return FORMAT_DATE.format(forDate.getTime());
        }
    }

    public static String date2weekday(Calendar forDate)
    {
        if (forDate == null)
        {
            return "n/a";
        }
        else
        {
            return FORMAT_WEEKDAY.format(forDate.getTime());
        }
    }
}
