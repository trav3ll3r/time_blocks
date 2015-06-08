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
                TimeBlockEvent gap = event.createGapBeforeStart(midnight.getTime());
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

    public static List<TimeBlockEvent> generateEmptyBlocks(List<TimeBlockEvent> events)
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
                TimeBlockEvent gap = event.createGapBeforeStart(midnight.getTime());
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

    public static List<TimeBlockEvent> generateBlocksInside(Calendar calStart, Calendar calEnd)
    {
        List<TimeBlockEvent> result = new ArrayList<TimeBlockEvent>();

        Date blockStartTime;
        Date blockEndTime;
        TimeBlockEvent event;
        int startMinutes;
        int minutesToCompleteBlock;

        if (calStart != null && calEnd != null)
        {
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
        }

        return result;
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
