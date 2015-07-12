package au.com.traveller.timeblocks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TimeBlocksFragment extends Fragment implements TimeBlocks
{
    protected String TAG = TimeBlocksFragment.class.toString();

    // FRAGMENT ARGUMENTS
    protected static final String ARG_FOR_DATE = "for_date";

    // FRAGMENT PROPERTIES (DEFINED VIA ARGUMENTS)
    protected Calendar _forDate;

    private List<TimeBlock> _guidelineBlocks;
    private List<TimeBlockEvent> _eventBlocks;

    private RelativeLayout _timeBlockHeading;
    private LinearLayout _timeBlockGuidelines;
    private LinearLayout _timeBlockEvents;

    public static TimeBlocksFragment newInstance(Calendar forDate)
    {
        TimeBlocksFragment f = new TimeBlocksFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FOR_DATE, forDate);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            this._forDate = (Calendar) getArguments().getSerializable(ARG_FOR_DATE);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.tb_fragment_one_day, container, false);

        this.initResources(rootView);
        this.renderTimeBlockPageHeader();
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

    private void renderTimeBlockPageHeader()
    {
        if (this.getShowPageHeader())
        {
            if (this._forDate == null)
            {
                this.errorHandler("Error in renderTimeBlockPageHeader", new NullPointerException("_forDate is null"));
            }

            if (getActivity() == null)
            {
                this.errorHandler("Error in renderTimeBlockPageHeader", new NullPointerException("getActivity() returned null"));
            }

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            final ViewGroup v = this.getPageHeaderView(inflater, _timeBlockHeading, this._forDate);

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

    private void renderTimeBlockEvents()
    {
        Log.i(TAG, "renderTimeBlockEvents for date: " + UiUtil.date2string(this._forDate));

        this._eventBlocks = this.generateTimeBlockEvents();
        this._eventBlocks = TimeBlockUtil.generateEmptyBlocks(this._eventBlocks, this._forDate);

        this._timeBlockEvents.removeAllViews();

        for (TimeBlockEvent event : _eventBlocks)
        {
            View eventView = this.getCompleteEventRow(getActivity(), _timeBlockEvents, event);
            if (eventView != null)
            {
                this._timeBlockEvents.addView(eventView);
            }
        }
    }

    public boolean getShowPageHeader()
    {
        return true;
    }

    public ViewGroup getPageHeaderView(LayoutInflater inflater, ViewGroup parent, Calendar forDate)
    {
        ViewGroup v;
        try
        {
            v = (LinearLayout) inflater.inflate(R.layout.tb_part_heading, parent, false);
            TextView t1 = (TextView) v.findViewById(R.id.heading_label_1);
            TextView t2 = (TextView) v.findViewById(R.id.heading_label_2);

            t1.setText(UiUtil.date2string(forDate));
            t2.setText(UiUtil.date2weekday(forDate));
        }
        catch (Exception ex)
        {
            v = null;
            this.errorHandler("Failed to render PageHeaderView", ex);
        }

        return v;
    }

    public final void renderEvents()
    {
        this.renderTimeBlockEvents();
    }

    @Override
    public List<TimeBlockEvent> generateTimeBlockEvents()
    {
        return new ArrayList<TimeBlockEvent>();
    }

    @Override
    public ViewGroup getEventView(LayoutInflater inflater, ViewGroup parentView, TimeBlockEvent event)
    {
        ViewGroup v;
        try
        {
            v = (LinearLayout) inflater.inflate(R.layout.tb_part_event_content, parentView, false);

            TextView tv = (TextView) v.findViewById(R.id.time_block_event_label);
            tv.setText(event.getEventLabel());
            tv.setBackgroundColor(getResources().getColor(event.getBackgroundColour()));
        }
        catch (Exception ex)
        {
            v = null;
            this.errorHandler("Error in getEventView", ex);
        }

        return v;
    }

    private ViewGroup getCompleteEventRow(Context context, ViewGroup parentView, TimeBlockEvent event)
    {
        ViewGroup v;
        try
        {
            LayoutInflater inflater = LayoutInflater.from(context);

            v = (RelativeLayout) inflater.inflate(R.layout.tb_part_full_event, parentView, false);

            int eventHeight = (int) getResources().getDimension(R.dimen.tb_block_hour_height_dip);
            eventHeight = (int) (eventHeight * event.durationInHours());
            event.setHeightInDip(eventHeight);

            v.getLayoutParams().height = UiUtil.dipToPixels(context, event.getHeightInDip());

            FrameLayout frame = (FrameLayout) v.findViewById(R.id.frame_time_block_event);
            frame.setPadding((int) getResources().getDimension(R.dimen.tb_block_indicator_width_dip), 1, 1, 1);

            // SET CONTENT BACKGROUND
            View contentView = this.getEventView(inflater, v, event);
            if (contentView != null)
            {
                contentView.setBackgroundColor(getResources().getColor(event.getBackgroundColour()));

                // ADD THE EVENT CONTENT VIEW
                frame.addView(contentView);
            }
        }
        catch (Exception ex)
        {
            v = null;
            this.errorHandler(String.format("Error in getCompleteEventRow for: %s. Are you sure the fragment has been initialised and/or has not been destroyed?", UiUtil.date2string(this._forDate)), ex);
        }

        return v;
    }

    public void errorHandler(final String message, final Exception ex)
    {
        String exceptionMessage = "";
        if (ex != null && ex.getMessage() != null)
        {
            exceptionMessage = ex.getMessage();
        }
        String log = String.format("%s: %s", message, exceptionMessage);
        Log.e(TAG, log);
    }
}