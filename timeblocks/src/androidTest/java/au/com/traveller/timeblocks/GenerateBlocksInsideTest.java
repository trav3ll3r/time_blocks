package au.com.traveller.timeblocks;

import android.test.InstrumentationTestCase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GenerateBlocksInsideTest extends InstrumentationTestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    public void test_null_start() throws Exception
    {
        this.setName("start null, 0 blocks");

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2015, 6, 7, 15, 0);
        List<TimeBlockEvent> result = TimeBlockUtil.generateBlocksInside(null, calEnd);

        assertEquals(0, result.size());
    }

    public void test_null_end() throws Exception
    {
        this.setName("end null, 0 blocks");

        Calendar calStart = Calendar.getInstance();
        calStart.set(2015, 6, 7, 5, 0);
        List<TimeBlockEvent> result = TimeBlockUtil.generateBlocksInside(calStart, null);

        assertEquals(0, result.size());
    }

    public void test_null_both() throws Exception
    {
        this.setName("both null, 0 blocks");
        List<TimeBlockEvent> result = TimeBlockUtil.generateBlocksInside(null, null);
        assertEquals(0, result.size());
    }

    public void test_0000_to_0800() throws Exception
    {
        this.setName("00:00 - 08:00, 8 blocks");
        Calendar calStart = Calendar.getInstance();
        calStart.set(2015, 6, 7, 0, 0);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2015, 6, 7, 8, 0);
        List<TimeBlockEvent> result = TimeBlockUtil.generateBlocksInside(calStart, calEnd);

        assertEquals(8, result.size());
    }

    public void test_0000_to_1000() throws Exception
    {
        this.setName("00:00 - 10:00, 10 blocks");
        Date start = new Date();

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(start);
        calStart.set(Calendar.HOUR, 0);
        calStart.set(Calendar.MINUTE, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(start);
        calEnd.set(Calendar.HOUR, 10);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.MILLISECOND, 238);  // THIS CAUSED AN ISSUE (FIX: THE generateBlocksInside RESETS IT TO ZERO)

        List<TimeBlockEvent> result = TimeBlockUtil.generateBlocksInside(calStart, calEnd);

        assertEquals(10, result.size());
    }

    public void test_0130_to_0230() throws Exception
    {
        this.setName("01:30 - 02:30, 2 blocks");
        Calendar calStart = Calendar.getInstance();
        calStart.set(2015, 6, 7, 1, 30);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2015, 6, 7, 2, 30);
        List<TimeBlockEvent> result = TimeBlockUtil.generateBlocksInside(calStart, calEnd);

        assertEquals(2, result.size());
        assertEquals(30.0, result.get(0).durationInMinutes());
        assertEquals(30.0, result.get(1).durationInMinutes());
    }

    public void test_0153_to_0216() throws Exception
    {
        this.setName("01:53 - 02:16, 2 blocks");
        Calendar calStart = Calendar.getInstance();
        calStart.set(2015, 6, 7, 1, 53);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2015, 6, 7, 2, 16);
        List<TimeBlockEvent> result = TimeBlockUtil.generateBlocksInside(calStart, calEnd);

        assertEquals(2, result.size());
        assertEquals(7.0, result.get(0).durationInMinutes());
        assertEquals(16.0, result.get(1).durationInMinutes());
    }

    public void test_2250_to_0000() throws Exception
    {
        this.setName("22:50 - 00:00, 2 blocks");
        Calendar calStart = Calendar.getInstance();
        calStart.set(2015, 6, 7, 22, 50);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2015, 6, 8, 0, 0);
        List<TimeBlockEvent> result = TimeBlockUtil.generateBlocksInside(calStart, calEnd);

        assertEquals(2, result.size());
        assertEquals(10.0, result.get(0).durationInMinutes());
        assertEquals(60.0, result.get(1).durationInMinutes());
    }

    public void test_2307_to_0000() throws Exception
    {
        this.setName("23:07 - 00:00, 2 blocks");
        Calendar calStart = Calendar.getInstance();
        calStart.set(2015, 6, 7, 23, 7);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2015, 6, 8, 0, 0);
        List<TimeBlockEvent> result = TimeBlockUtil.generateBlocksInside(calStart, calEnd);

        assertEquals(1, result.size());
        assertEquals(53.0, result.get(0).durationInMinutes());
    }

    public void test_2310_to_0030() throws Exception
    {
        this.setName("23:10 - 00:30, 2 blocks");
        Calendar calStart = Calendar.getInstance();
        calStart.set(2015, 6, 7, 23, 10);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2015, 6, 8, 0, 30);
        List<TimeBlockEvent> result = TimeBlockUtil.generateBlocksInside(calStart, calEnd);

        assertEquals(2, result.size());
        assertEquals(50.0, result.get(0).durationInMinutes());
        assertEquals(30.0, result.get(1).durationInMinutes());
    }

    public void test_1601_to_1700() throws Exception
    {
        this.setName("16:01 - 17:00, 1 blocks");
        Calendar calStart = Calendar.getInstance();
        calStart.set(2015, 6, 7, 16, 1);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2015, 6, 7, 17, 0);
        List<TimeBlockEvent> result = TimeBlockUtil.generateBlocksInside(calStart, calEnd);

        assertEquals(1, result.size());
        assertEquals(59.0, result.get(0).durationInMinutes());
    }
}
