package au.com.traveller.timeblocks;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public interface TimeBlocks
{
    List<TimeBlockEvent> generateTimeBlockEvents();
    View getEventView(Context context, ViewGroup parentView, TimeBlockEvent event);
}
