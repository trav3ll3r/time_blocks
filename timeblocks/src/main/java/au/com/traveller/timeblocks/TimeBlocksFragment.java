package au.com.traveller.timeblocks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeBlocksFragment extends Fragment implements TimeBlocks
{
    private final String TAG = TimeBlocksFragment.class.toString();

    private List<TimeBlock> _guidelineBlocks;
    private List<TimeBlockEvent> _eventBlocks;

    private RelativeLayout _timeBlockHeading;
    private LinearLayout _timeBlockGuidelines;
    private LinearLayout _timeBlockEvents;

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.tb_fragment_one_day, container, false);

        this.initResources(rootView);
        this.renderTimeBlocksGuidelines();

        return rootView;
    }

    private void initResources(View rootView)
    {
        _timeBlockHeading    = (RelativeLayout) rootView.findViewById(R.id.time_blocks_heading);
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
            tv.getLayoutParams().width = (int) getResources().getDimension(R.dimen.tb_block_indicator_width_dip);

            _timeBlockGuidelines.addView(guidelineView);
        }
    }

    private void renderTimeBlockHeading(Calendar forDate)
    {
        if (this.getShowHeading())
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            final ViewGroup v = this.getHeadingView(inflater, _timeBlockHeading, forDate);

            this.resizeScheduleHeading(v);
        }
    }

    private void resizeScheduleHeading(ViewGroup v)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _timeBlockHeading.getLayoutParams();
//        params.height = v.getHeight();
        int eventHeight = (int) getResources().getDimension(R.dimen.tb_block_hour_height_dip);
        params.height = UiUtil.dipToPixels(getActivity(), eventHeight);
        _timeBlockHeading.setLayoutParams(params);

        _timeBlockHeading.removeAllViews();
        _timeBlockHeading.addView(v);
    }

    private void renderTimeBlockEvents(Calendar forDate)
    {
        this._eventBlocks = this.generateTimeBlockEvents();
        this._eventBlocks = TimeBlockUtil.generateEmptyBlocks(this._eventBlocks, forDate);

        this._timeBlockEvents.removeAllViews();

        for (TimeBlockEvent event : _eventBlocks)
        {
            View eventView = getCompleteEventRow(getActivity(), _timeBlockEvents, event);
            this._timeBlockEvents.addView(eventView);
        }
    }

    public boolean getShowHeading()
    {
        return true;
    }

    public ViewGroup getHeadingView(LayoutInflater inflater, ViewGroup parent, Calendar forDate)
    {
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.tb_part_heading, parent, false);
        TextView t = (TextView) v.findViewById(R.id.heading_label);
        t.setText(String.format("%s-%s-%s", forDate.get(Calendar.YEAR), forDate.get(Calendar.MONTH)+1, forDate.get(Calendar.DAY_OF_MONTH)));
        return v;
    }

    public final void renderEvents(Calendar forDate)
    {
        this.renderTimeBlockHeading(forDate);
        this.renderTimeBlockEvents(forDate);
    }

    @Override
    public List<TimeBlockEvent> generateTimeBlockEvents()
    {
        return new ArrayList<TimeBlockEvent>();
    }

    @Override
    public View getEventView(Context context, ViewGroup parentView, TimeBlockEvent event)
    {
        LinearLayout eventContent = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tb_part_event_content, parentView, false);

        TextView tv = (TextView) eventContent.findViewById(R.id.time_block_event_label);
        tv.setText(event.getEventLabel());
        tv.setBackgroundColor(getResources().getColor(event.getBackgroundColour()));

        return eventContent;
    }

    private View getCompleteEventRow(Context context, ViewGroup parentView, TimeBlockEvent event)
    {
        ViewGroup row = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.tb_part_full_event, parentView, false);

        int eventHeight = (int) getResources().getDimension(R.dimen.tb_block_hour_height_dip);
        eventHeight = (int) (eventHeight * event.durationInHours());
        event.setHeightInDip(eventHeight);

        row.getLayoutParams().height = UiUtil.dipToPixels(context, event.getHeightInDip());

        FrameLayout frame = (FrameLayout) row.findViewById(R.id.frame_time_block_event);
        frame.setPadding((int) getResources().getDimension(R.dimen.tb_block_indicator_width_dip), 1, 1, 1);

        // SET CONTENT BACKGROUND
        View contentView = getEventView(context, row, event);
        contentView.setBackgroundColor(getResources().getColor(event.getBackgroundColour()));

        // ADD THE EVENT CONTENT VIEW
        frame.addView(contentView);

        return row;
    }
}