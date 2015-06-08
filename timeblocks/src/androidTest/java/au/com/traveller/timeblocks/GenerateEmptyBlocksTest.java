package au.com.traveller.timeblocks;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GenerateEmptyBlocksTest extends InstrumentationTestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    public void test_null_events() throws Exception
    {
        this.setName("null events, 0 blocks");

        Calendar forDate = Calendar.getInstance();
        forDate.set(2015, 6, 7, 15, 22, 18);
        forDate.set(Calendar.MILLISECOND, 10);

        List<TimeBlockEvent> result = TimeBlockUtil.generateEmptyBlocks(null, forDate);

        assertEquals(0, result.size());
    }

    public void test_null_forDate() throws Exception
    {
        this.setName("forDate null, 0 blocks");

        List<TimeBlockEvent> events = new ArrayList<TimeBlockEvent>();
        List<TimeBlockEvent> result = TimeBlockUtil.generateEmptyBlocks(events, null);

        assertEquals(0, result.size());
    }

    public void test_empty_events() throws Exception
    {
        this.setName("empty events, 24 blocks");

        Calendar forDate = Calendar.getInstance();
        forDate.set(2015, 6, 7, 15, 22, 18);
        forDate.set(Calendar.MILLISECOND, 10);

        List<TimeBlockEvent> events = new ArrayList<TimeBlockEvent>();
        List<TimeBlockEvent> result = TimeBlockUtil.generateEmptyBlocks(events, forDate);

        assertEquals(24, result.size());
    }

    public void test_1_full_hour_event() throws Exception
    {
        this.setName("1 full hour event, 24 blocks");

        Calendar forDate = Calendar.getInstance();
        forDate.set(2015, 6, 7, 15, 22, 18);
        forDate.set(Calendar.MILLISECOND, 10);

        Calendar start = Calendar.getInstance();
        start.set(2015, 6, 7, 10, 0);
        Calendar end = Calendar.getInstance();
        end.set(2015, 6, 7, 11, 0);

        List<TimeBlockEvent> events = new ArrayList<TimeBlockEvent>();
        events.add(new TimeBlockEvent(start, end));
        List<TimeBlockEvent> result = TimeBlockUtil.generateEmptyBlocks(events, forDate);

        assertEquals(24, result.size());
    }

    public void test_1_half_hour_event() throws Exception
    {
        this.setName("1 half-hour event, 25 blocks");

        Calendar forDate = Calendar.getInstance();
        forDate.set(2015, 6, 7, 15, 22, 18);
        forDate.set(Calendar.MILLISECOND, 10);

        Calendar start = Calendar.getInstance();
        start.set(2015, 6, 7, 10, 0);
        Calendar end = Calendar.getInstance();
        end.set(2015, 6, 7, 10, 30);

        List<TimeBlockEvent> events = new ArrayList<TimeBlockEvent>();
        events.add(new TimeBlockEvent(start, end));
        List<TimeBlockEvent> result = TimeBlockUtil.generateEmptyBlocks(events, forDate);

        assertEquals(25, result.size());
    }

    public void test_1_hour_and_half_event() throws Exception
    {
        this.setName("1 hour and a half event, 24 blocks");

        Calendar forDate = Calendar.getInstance();
        forDate.set(2015, 6, 7, 15, 22, 18);
        forDate.set(Calendar.MILLISECOND, 10);

        Calendar start = Calendar.getInstance();
        start.set(2015, 6, 7, 10, 0);
        Calendar end = Calendar.getInstance();
        end.set(2015, 6, 7, 11, 30);

        List<TimeBlockEvent> events = new ArrayList<TimeBlockEvent>();
        events.add(new TimeBlockEvent(start, end));
        List<TimeBlockEvent> result = TimeBlockUtil.generateEmptyBlocks(events, forDate);

        assertEquals(24, result.size());
    }

    public void test_3_hours_event() throws Exception
    {
        this.setName("3 hours event, 22 blocks");

        Calendar forDate = Calendar.getInstance();
        forDate.set(2015, 6, 7, 15, 22, 18);
        forDate.set(Calendar.MILLISECOND, 10);

        Calendar start = Calendar.getInstance();
        start.set(2015, 6, 7, 10, 0);
        Calendar end = Calendar.getInstance();
        end.set(2015, 6, 7, 13, 0);

        List<TimeBlockEvent> events = new ArrayList<TimeBlockEvent>();
        events.add(new TimeBlockEvent(start, end));
        List<TimeBlockEvent> result = TimeBlockUtil.generateEmptyBlocks(events, forDate);

        assertEquals(22, result.size());
    }

    public void test_3_hours_and_half_event() throws Exception
    {
        this.setName("3 hours and a half event, 22 blocks");

        Calendar forDate = Calendar.getInstance();
        forDate.set(2015, 6, 7, 15, 22, 18);
        forDate.set(Calendar.MILLISECOND, 10);

        Calendar start = Calendar.getInstance();
        start.set(2015, 6, 7, 10, 0);
        Calendar end = Calendar.getInstance();
        end.set(2015, 6, 7, 13, 30);

        List<TimeBlockEvent> events = new ArrayList<TimeBlockEvent>();
        events.add(new TimeBlockEvent(start, end));
        List<TimeBlockEvent> result = TimeBlockUtil.generateEmptyBlocks(events, forDate);

        assertEquals(22, result.size());
    }
}
