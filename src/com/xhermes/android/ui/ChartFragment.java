package com.xhermes.android.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.xhermes.android.R;

import java.util.Calendar;


/**
 * Created by rogers on 3/18/14.
 */
public class ChartFragment extends Fragment {

	private String terminalId;
	private String dataStr;
	private String description;
	private TextView description_in_chart;
	private LinearLayout layout;
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		description_in_chart.setText(description);
        String data[] = dataStr.split(",");
        GraphView.GraphViewData viewdata[] = new GraphView.GraphViewData[data.length];
        //int horizontal_strs_length = (int) Math.ceil((data.length-1)*1.00/5.00) + 1;
        int horizontal_strs_length = data.length;
        String[] horizontal_strs = new String[horizontal_strs_length];
        double maxData = 0;
        for(int i=0;i<viewdata.length;i++){
        	viewdata[i] = new GraphView.GraphViewData(i,Double.parseDouble(data[i]));
        	maxData = Double.parseDouble(data[i])>maxData?Double.parseDouble(data[i]):maxData;
        }
        for(int i=0;i<horizontal_strs_length;i++){
        	if((i+1)%5==0)
        		horizontal_strs[i] = i + 1 + "";
        	else
        		horizontal_strs[i] = "";
        }
        String[] verticle_strs;
        if(maxData<=15){
        	verticle_strs = new String[(int) Math.ceil(maxData) + 1];
        	for(int i=0;i<verticle_strs.length;i++){
        		verticle_strs[verticle_strs.length - i - 1] = i+"";
        	}
        }
        else{
        	int count_times = (int) Math.ceil(maxData/15);
        	verticle_strs = new String[16];
        	for(int i=0;i<verticle_strs.length;i++){
        		verticle_strs[verticle_strs.length - i - 1] = i * count_times + "";
        	}
        }
        GraphViewSeries exampleSeries = new GraphViewSeries(viewdata);

        GraphView graphView = new LineGraphView(
                getActivity().getApplicationContext() // context
                , "" // heading
        );

        graphView.addSeries(exampleSeries); // data
        graphView.setHorizontalLabels(horizontal_strs);
        graphView.setVerticalLabels(verticle_strs);
        graphView.setManualYAxisBounds(Double.parseDouble(verticle_strs[0]),0.0);
        graphView.getGraphViewStyle().setHorizontalLabelsColor(getResources().getColor(R.color.black));
        graphView.getGraphViewStyle().setVerticalLabelsColor(getResources().getColor(R.color.black));
        layout.addView(graphView);
	}
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		terminalId = getArguments().getString("terminalId");
		dataStr = getArguments().getString("dataStr");
		description = getArguments().getString("description");
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        description_in_chart = (TextView) view.findViewById(R.id.description_in_chart);
        layout = (LinearLayout) view.findViewById(R.id.graph);
        return view;
    }
}
