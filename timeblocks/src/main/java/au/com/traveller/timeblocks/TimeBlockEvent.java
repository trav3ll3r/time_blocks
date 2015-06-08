package au.com.traveller.timeblocks;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class TimeBlockEvent implements Serializable
{
    private Date startTime;
    private Date endTime;
    private String eventLabel;
    private int heightInDip;
    private int backgroundColour;
    private Object tag;

    public TimeBlockEvent(Date startTime, Date endTime)
    {
        this.startTime          = startTime;
        this.endTime            = endTime;
        this.backgroundColour   = R.color.calendar_lesson_free;
    }

    public TimeBlockEvent(Calendar startCalendar, Calendar endCalendar)
    {
        this(startCalendar.getTime(), endCalendar.getTime());
    }

    public String getEventLabel() {
        return eventLabel;
    }

    public void setEventLabel(String value) {
        this.eventLabel = value;
    }

    public int getHeightInDip() {
        return heightInDip;
    }

    public void setHeightInDip(int value) {
        this.heightInDip = value;
    }

    public int getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(int value) {
        this.backgroundColour = value;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date value) {
        this.startTime = value;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date value) {
        this.endTime = value;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object value) {
        this.tag = value;
    }

    public double durationInMinutes()
    {
        long millis = (this.endTime.getTime() - this.startTime.getTime());
        return (millis / 1000.0) / 60.0;
    }

    public double durationInHours()
    {
        long millis = (this.endTime.getTime() - this.startTime.getTime());
        return (millis / 1000.0) / 3600.0;
    }


}
