package au.com.traveller.timeblocks;

public class TimeBlock
{
    private String timeLabel;
    private int heightInDip;
    private int backgroundColour;

    public TimeBlock()
    {
        this.backgroundColour = R.color.calendar_any_day_background;
    }

    public String getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(String value) {
        this.timeLabel = value;
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
}
