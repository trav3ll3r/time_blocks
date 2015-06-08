package au.com.traveller.timeblocks;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeBlockUtil
{
    public static List<TimeBlock> generateGuidelines(Context context)
    {
        List<TimeBlock> result = new ArrayList<TimeBlock>();
        TimeBlock block;

        int blockHeight = (int) context.getResources().getDimension(R.dimen.tb_block_hour_height_dip);

        block = new TimeBlock();
        block.setHeightInDip(blockHeight);
        block.setTimeLabel(String.format("%s AM", 12));
        result.add(block);

        for (int i=1; i<=11; i++)
        {
            block = new TimeBlock();
            block.setHeightInDip(blockHeight);
            block.setTimeLabel(String.format("%s AM", i));
            result.add(block);
        }

        block = new TimeBlock();
        block.setHeightInDip(blockHeight);
        block.setTimeLabel(String.format("%s PM", 12));
        result.add(block);

        for (int i=1; i<=11; i++)
        {
            block = new TimeBlock();
            block.setHeightInDip(blockHeight);
            block.setTimeLabel(String.format("%s PM", i));
            result.add(block);
        }

        return result;
    }

    public static List<TimeBlockEvent> fillEventGaps(List<TimeBlockEvent> events)
    {
        List<TimeBlockEvent> result = new ArrayList<TimeBlockEvent>();

        TimeBlockEvent prev = null;
        for (TimeBlockEvent event : events)
        {
            if (prev != null)
            {
                int gapInMinutes = TimeBlockUtil.gapInMinutes(prev, event);
                if (gapInMinutes > 1)
                {
                    TimeBlockEvent gap = new TimeBlockEvent(prev.getEndTime(), event.getStartTime());
                    result.add(gap);
                }
            }
            else
            {
                // CHECK IF GAP IS NEEDED BEFORE MIDNIGHT AND FIRST EVENT
                Calendar midnight = Calendar.getInstance();
                midnight.setTime(event.getStartTime());
                midnight.set(Calendar.HOUR_OF_DAY, 0);
                midnight.set(Calendar.MINUTE, 0);
                midnight.set(Calendar.SECOND, 0);
                midnight.set(Calendar.MILLISECOND, 0);
                TimeBlockEvent gap = new TimeBlockEvent(midnight.getTime(), event.getStartTime());
                if (gap.durationInMinutes() > 1)
                {
                    result.add(gap);
                }
            }

            prev = event;
            result.add(event);
        }

        return result;
    }

    public static List<TimeBlockEvent> generateEmptyBlocks(List<TimeBlockEvent> events, Calendar forDate)
    {
        List<TimeBlockEvent> result = new ArrayList<TimeBlockEvent>();

        TimeBlockEvent lastEvent = null;
        Calendar calGapStart;
        Calendar calGapEnd;

        // PREVENT INVALID ARGUMENTS
        if (events == null || forDate == null)
        {
            return result;
        }

        for (TimeBlockEvent event : events)
        {
            calGapStart = Calendar.getInstance();

            calGapEnd = Calendar.getInstance();
            calGapEnd.setTime(event.getStartTime());

            if (lastEvent == null)
            {
                // FILL GAPS BETWEEN MIDNIGHT AND FIRST event
                calGapStart.setTimeInMillis(calGapEnd.getTimeInMillis());
                calGapStart = setToMidnight(calGapStart);
            }
            else
            {
                calGapStart.setTime(lastEvent.getEndTime());
            }

            // ADD ALL BLOCKS TO FILL THE GAP
            result.addAll(TimeBlockUtil.generateBlocksInside(calGapStart, calGapEnd));

            // ADD THE EVENT AFTER THE GAP
            result.add(event);

            lastEvent = event;
        }

        // ADD BLOCKS TO NEXT MIDNIGHT
        calGapStart = Calendar.getInstance();
        calGapEnd = Calendar.getInstance();
        if (lastEvent != null)
        {
            // ADD BLOCKS STARTING AT THE END OF LAST EVENT
            calGapStart.setTime(lastEvent.getEndTime());
            calGapEnd.setTime(lastEvent.getEndTime());
        }
        else
        {
            // ADD BLOCKS STARTING AT THE MIDNIGHT OF forDate
            calGapStart.setTimeInMillis(forDate.getTimeInMillis());
            calGapEnd.setTimeInMillis(forDate.getTimeInMillis());

            calGapStart = setToMidnight(calGapStart);
        }
        calGapEnd = setToMidnight(calGapEnd);
        calGapEnd.add(Calendar.HOUR, 24);
        result.addAll(TimeBlockUtil.generateBlocksInside(calGapStart, calGapEnd));

        return result;
    }

    /**
     * Generated 60-minutes TimeBlocks between {@code calStart} and {@code calEnd}
     * @param calStart Calendar object (not null)
     * @param calEnd Calendar object (not null, not before {@code calStart})
     * @return A list of time blocks (or empty list if {@code calStart} and {@code calEnd} are invalid)
     */
    public static List<TimeBlockEvent> generateBlocksInside(Calendar calStart, Calendar calEnd)
    {
        List<TimeBlockEvent> result = new ArrayList<TimeBlockEvent>();

        Date blockStartTime;
        Date blockEndTime;
        TimeBlockEvent event;
        int startMinutes;
        int minutesToCompleteBlock;

        // HANDLE INVALID USAGES
        if (calStart == null || calEnd == null)
        {
            return result;
        }

        calStart = roundDownToWholeMinute(calStart);
        calEnd = roundDownToWholeMinute(calEnd);

        if (calStart.getTimeInMillis() >= calEnd.getTimeInMillis())
        {
            return result;
        }

        while (calStart.getTimeInMillis() < calEnd.getTimeInMillis())
        {
            minutesToCompleteBlock = 60;

            startMinutes = calStart.get(Calendar.MINUTE);

            if (startMinutes > 0 && startMinutes < minutesToCompleteBlock)
            {
                minutesToCompleteBlock -= startMinutes;
            }

            blockStartTime = calStart.getTime();
            calStart.add(Calendar.MINUTE, minutesToCompleteBlock);

            if (calStart.getTimeInMillis() > calEnd.getTimeInMillis())
            {
                calStart = calEnd;
            }

            blockEndTime = calStart.getTime();

            event = new TimeBlockEvent(blockStartTime, blockEndTime);
            result.add(event);
        }

        return result;
    }

    /**
     * Sets Calendar time component of SECOND and MILLISECOND to ZERO
     * @param cal Calendar (not null)
     * @return Calendar with (time) component "HH:mm:00.0"
     */
    private static Calendar roundDownToWholeMinute(Calendar cal)
    {
        if (cal != null)
        {
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
        }
        return cal;
    }

    private static Calendar setToMidnight(Calendar cal)
    {
        if (cal != null)
        {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
        return cal;
    }

    private static int gapInMinutes(TimeBlockEvent prev, TimeBlockEvent next)
    {
        if (prev != null && next != null)
        {
            long millis = next.getStartTime().getTime() - prev.getEndTime().getTime();
            return (int) Math.ceil((millis / 1000) / 60);
        }

        return -1;
    }
}
