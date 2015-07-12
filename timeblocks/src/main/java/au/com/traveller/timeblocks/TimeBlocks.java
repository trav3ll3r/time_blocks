package au.com.traveller.timeblocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public interface TimeBlocks
{
    List<TimeBlockEvent> generateTimeBlockEvents();
    View getEventView(LayoutInflater inflater, ViewGroup parentView, TimeBlockEvent event);
}
