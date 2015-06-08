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

    public void test_no_events() throws Exception
    {
        this.setName("start null, 0 blocks");

        Calendar forDate = Calendar.getInstance();
        forDate.set(2015, 6, 7, 15, 22, 18);
        forDate.set(Calendar.MILLISECOND, 10);

        List<TimeBlockEvent> events = new ArrayList<TimeBlockEvent>();
        List<TimeBlockEvent> result = TimeBlockUtil.generateEmptyBlocks(events, forDate);

        assertEquals(24, result.size());
    }

}
