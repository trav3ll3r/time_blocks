package au.com.traveller.timeblocks;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TimeBlocksFragment extends Fragment
{
    private final String TAG = TimeBlocksFragment.class.toString();

    private List<TimeBlock> _guidelineBlocks;
    private List<TimeBlockEvent> _eventBlocks;

    private LinearLayout _timeBlockGuidelines;
    private LinearLayout _timeBlockEvents;

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.tb_fragment_one_day, container, false);

        this.initResources(rootView);
        this.renderTimeBlocksGuidelines();

        this.refreshEvents();

        return rootView;
    }

    private void initResources(View rootView)
    {
        _timeBlockGuidelines = (LinearLayout) rootView.findViewById(R.id.time_blocks_guidelines);
        _timeBlockEvents     = (LinearLayout) rootView.findViewById(R.id.time_blocks_events);
    }

    private void renderTimeBlocksGuidelines()
    {
        _guidelineBlocks = TimeBlockUtil.generateGuidelines(getActivity());

        for (TimeBlock block : _guidelineBlocks)
        {
            LinearLayout guidelineView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tb_part_guideline, _timeBlockGuidelines, false);
            guidelineView.getLayoutParams().height = UiUtil.dipToPixels(getActivity(), block.getHeightInDip());

            TextView tv = (TextView) guidelineView.findViewById(R.id.time_block_row);
            tv.setText(block.getTimeLabel());
            tv.getLayoutParams().width =(int) getResources().getDimension(R.dimen.tb_block_indicator_width_dip);

            _timeBlockGuidelines.addView(guidelineView);
        }
    }

    private void renderTimeBlockEvents()
    {
        this._eventBlocks = this.generateTimeBlockEvents();
        this._eventBlocks = TimeBlockUtil.fillEventGaps(this._eventBlocks);

        for (TimeBlockEvent event : _eventBlocks)
        {
            View eventView = getEventView(getActivity(), _timeBlockEvents, event);

            eventView.setPadding((int) getResources().getDimension(R.dimen.tb_block_indicator_width_dip), 1, 1, 1);

            this._timeBlockEvents.addView(eventView);
        }
    }

    public final void refreshEvents()
    {
        this.getTimeBlocksData();
        this.renderTimeBlockEvents();
    }

    //TODO: @Override
    protected void getTimeBlocksData()
    {
    }

    //TODO: @Override
    protected List<TimeBlockEvent> generateTimeBlockEvents()
    {
        return new ArrayList<TimeBlockEvent>();
    }

    //TODO: @Override
    protected View getEventView(Context context, ViewGroup parentView, TimeBlockEvent event)
    {
        LinearLayout eventView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tb_part_event, parentView, false);

        int eventHeight = (int) getResources().getDimension(R.dimen.tb_block_hour_height_dip);
        eventHeight = (int) (eventHeight * event.durationInHours());
        event.setHeightInDip(eventHeight);

        eventView.getLayoutParams().height = UiUtil.dipToPixels(context, event.getHeightInDip());

        TextView tv = (TextView) eventView.findViewById(R.id.time_block_event);
        tv.setText(event.getEventLabel());
        tv.setBackgroundColor(getResources().getColor(event.getBackgroundColour()));

        return eventView;
    }
}