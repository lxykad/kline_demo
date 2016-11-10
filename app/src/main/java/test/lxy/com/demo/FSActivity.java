package test.lxy.com.demo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import test.lxy.com.demo.bean.FsBean2;

/**
 * 分时图
 */

public class FSActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName().toString();
    private MyLineChart lineChart;
    private MyBarChart barChart;

    MyXAxis xAxisLine;
    MyYAxis axisRightLine;
    MyYAxis axisLeftLine;//x轴
    BarDataSet barDataSet;

    MyXAxis xAxisBar;
    MyYAxis axisLeftBar;
    MyYAxis axisRightBar;

    SparseArray<String> stringSparseArray;

    private LineDataSet d1, d2;

    private DataParse mData;

    private ArrayList<FsBean2> mList = new ArrayList<>();


    //
    private Button mBtConn;
    private Button mBtAdd;
    private Button mBt;
    private OkHttpClient mClient;
    //
    private static final String SOCKET_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kx);

        mBtConn = (Button) findViewById(R.id.bt_conn);
        mBtAdd = (Button) findViewById(R.id.bt_add_data);

        //创建OkHttpClient 对像
//        mClient = new OkHttpClient().newBuilder()
//                .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
//                .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
//                .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
//                .build();
        //
        //Request request = new Request.Builder().url(SOCKET_URL).build();

        //WebSocketCall webSocketCall = WebSocketCall.create(mOkHttpClient, request);


        initEvents();

        mData = new DataParse();
        initChart();

        //获取服务器历史数据
        loadDataFromServer();

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                barChart.highlightValues(new Highlight[]{h});
                lineChart.setHighlightValue(h);
            }

            @Override
            public void onNothingSelected() {
            }
        });
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                //  barChart.highlightValues(new Highlight[]{h});
                lineChart.setHighlightValue(new Highlight(h.getXIndex(), 0));//此函数已经返回highlightBValues的变量，并且刷新，故上面方法可以注释
                // barChart.setHighlightValue(h);
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    //连接服务器等操作
    private void initEvents() {
        //add data
        mBtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //连接服务器
        mBtConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //设置webSocket连接,回调


            }
        });
    }

    private void loadDataFromServer() {

        OkHttpUtils.get()
                .url("http://test.58caimi.com/v1/stock/timeseries/000001.SZ?lastNPoints=240")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println("beanbean222====eee====" + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {

//                        Gson gson = new Gson();
//                        ArrayList<FsBean2> list = new ArrayList<>();
//
//                        Type type = new TypeToken<ArrayList<FsBean2>>() {
//                        }.getType();
//
//                        list = gson.fromJson(response, type);

                        ArrayList<FsBean2> list = (ArrayList<FsBean2>) JSON.parseArray(response, FsBean2.class);
                        //mList = list;
                        for (int i = 0; i < 100; i++) {
                            mList.add(list.get(i));
//                            mData.parseMinutesData(list);
//                            setData(mData);

//                            try {
//                                Thread.sleep(200);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }

                        }
                        mData.parseMinutesData(mList);
                        setData(mData);


                    }
                });

    }


    private void initChart() {

        lineChart = (MyLineChart) findViewById(R.id.line_chart_demo);
        barChart = (MyBarChart) findViewById(R.id.bar_chart_demo);

        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.setBorderWidth(1);
        lineChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        //lineChart.setBorderColor(Color.parseColor("#ff0000"));
        lineChart.setDescription("");
        Legend lineChartLegend = lineChart.getLegend();
        lineChartLegend.setEnabled(false);//标注说明

        barChart.setScaleEnabled(false);
        barChart.setDrawBorders(true);
        barChart.setBorderWidth(1);
        barChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        barChart.setDescription("");


        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);
        //x轴
        xAxisLine = lineChart.getXAxis();
        xAxisLine.setDrawLabels(true);//时间标记
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);//标记在底部，默认在顶部
        //xAxisLine.setLabelsToSkip(59);


        //左边y
        axisLeftLine = lineChart.getAxisLeft();
        /*折线图y轴左没有basevalue，调用系统的*/
        axisLeftLine.setLabelCount(5, true);// 左边显示5条数据
        axisLeftLine.setDrawLabels(true);
        axisLeftLine.setDrawGridLines(false);//横线不显示
        /*轴不显示 避免和border冲突*/
        axisLeftLine.setDrawAxisLine(false);


        //右边y
        axisRightLine = lineChart.getAxisRight();
        axisRightLine.setLabelCount(2, true);//basevalue 上下各显示几条数据
        axisRightLine.setDrawLabels(true);
        axisRightLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                return mFormat.format(value);
            }
        });

        axisRightLine.setStartAtZero(false);
        axisRightLine.setDrawGridLines(false);
        axisRightLine.setDrawAxisLine(false);
        //背景线
        xAxisLine.setGridColor(getResources().getColor(R.color.minute_grayLine));
        //xAxisLine.setGridColor(Color.parseColor("#ff0000"));//时间竖线的颜色
        xAxisLine.setAxisLineColor(getResources().getColor(R.color.minute_grayLine));
        xAxisLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftLine.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisRightLine.setAxisLineColor(getResources().getColor(R.color.minute_grayLine));
        axisRightLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));

        //bar x y轴
        xAxisBar = barChart.getXAxis();
        xAxisBar.setDrawLabels(false);
        xAxisBar.setDrawGridLines(true);
        xAxisBar.setDrawAxisLine(false);
        // xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));


        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);
        //y轴样式
        this.axisLeftLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });

    }


    private SparseArray<String> setXLabels() {
//        int count = mData.getDatas().size() - 1;
//        int i = mData.getDatas().size() / 4;
//        int j = mData.getDatas().size() - 4 * i;
//        int rest = 4 * i + j;
//        SparseArray<String> xLabels = new SparseArray<>();
//        xLabels.put(0, "09:30");
//        xLabels.put(i, "10:30");
//        xLabels.put(2 * i, "11:30/13:00");
//        xLabels.put(3 * i, "14:00");
//        xLabels.put(count, "15:00");


        SparseArray<String> xLabels = new SparseArray<>();
        xLabels.put(0, "09:30");
        xLabels.put(60, "10:30");
        xLabels.put(120, "11:30/13:00");
        xLabels.put(180 , "14:00");
        xLabels.put(239, "15:00");


        return xLabels;
    }


    //设置分时图数据

    private void setMarkerView(DataParse mData) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(FSActivity.this, R.layout.mymarkerview);
        MyRightMarkerView rightMarkerView = new MyRightMarkerView(FSActivity.this, R.layout.mymarkerview);
        lineChart.setMarker(leftMarkerView, rightMarkerView, mData);
    }

    public void setShowLabels(SparseArray<String> labels) {
        xAxisLine.setXLabels(labels);
        xAxisBar.setXLabels(labels);
    }


    private void setData(DataParse mData) {
        stringSparseArray = setXLabels();

        setMarkerView(mData);//十字坐标
        setShowLabels(stringSparseArray);//开盘时间

        if (mData.getDatas().size() == 0) {
            lineChart.setNoDataText("暂无数据");
            return;
        }
        //设置y左右两轴最大最小值
        axisLeftLine.setAxisMinValue(mData.getMin());
        axisLeftLine.setAxisMaxValue(mData.getMax());
        axisRightLine.setAxisMinValue(mData.getPercentMin());
        axisRightLine.setAxisMaxValue(mData.getPercentMax());


        axisLeftBar.setAxisMaxValue(mData.getVolmax());
        /*单位*/
        String unit = MyUtils.getVolUnit(mData.getVolmax());
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        /*次方*/
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
        axisLeftBar.setShowMaxAndUnit(unit);
        axisLeftBar.setDrawLabels(true);
        //axisLeftBar.setAxisMinValue(0);//即使最小是不是0，也无碍
        axisLeftBar.setShowOnlyMinMax(true);
        axisRightBar.setAxisMaxValue(mData.getVolmax());
        //   axisRightBar.setAxisMinValue(mData.getVolmin);//即使最小是不是0，也无碍
        //axisRightBar.setShowOnlyMinMax(true);

        //基准线
        LimitLine ll = new LimitLine(0);
        ll.setLineWidth(1f);
        ll.setLineColor(getResources().getColor(R.color.minute_jizhun));
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLineWidth(1);
        axisRightLine.addLimitLine(ll);
        axisRightLine.setBaseValue(0);

        ArrayList<Entry> lineCJEntries = new ArrayList<>();
        ArrayList<Entry> lineJJEntries = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        Log.e("##", Integer.toString(xVals.size()));
        for (int i = 0, j = 0; i < mData.getDatas().size(); i++, j++) {
           /* //避免数据重复，skip也能正常显示
            if (mData.getDatas().get(i).time.equals("13:30")) {
                continue;
            }*/
            MinutesBean t = mData.getDatas().get(j);

            if (t == null) {
                lineCJEntries.add(new Entry(Float.NaN, i));
                lineJJEntries.add(new Entry(Float.NaN, i));
                barEntries.add(new BarEntry(Float.NaN, i));
                continue;
            }
            if (!TextUtils.isEmpty(stringSparseArray.get(i)) &&
                    stringSparseArray.get(i).contains("/")) {
                i++;
            }
            lineCJEntries.add(new Entry(mData.getDatas().get(i).cjprice, i));
            lineJJEntries.add(new Entry(mData.getDatas().get(i).avprice, i));
            barEntries.add(new BarEntry(mData.getDatas().get(i).cjnum, i));
            // dateList.add(mData.getDatas().get(i).time);
        }
        d1 = new LineDataSet(lineCJEntries, "成交价");
        d2 = new LineDataSet(lineJJEntries, "均价");
        d1.setDrawValues(false);
        d2.setDrawValues(false);
        barDataSet = new BarDataSet(barEntries, "成交量");

        d1.setCircleRadius(0);
        d2.setCircleRadius(0);
        d1.setColor(getResources().getColor(R.color.minute_blue));
        d2.setColor(getResources().getColor(R.color.minute_yellow));
        d1.setHighLightColor(Color.WHITE);
        d2.setHighlightEnabled(false);
        d1.setDrawFilled(true);


        barDataSet.setBarSpacePercent(50); //bar空隙
        barDataSet.setHighLightColor(Color.WHITE);
        barDataSet.setHighLightAlpha(255);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setColor(Color.RED);
        List<Integer> list = new ArrayList<>();
        list.add(Color.RED);
        list.add(Color.GREEN);
        barDataSet.setColors(list);
        //谁为基准
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        // d2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);
        /*注老版本LineData参数可以为空，最新版本会报错，修改进入ChartData加入if判断*/
        LineData cd = new LineData(getMinutesCount(), sets);
        lineChart.setData(cd);
        BarData barData = new BarData(getMinutesCount(), barDataSet);
        barChart.setData(barData);

        //设置量表对齐
        setOffset();
        //刷新图
        lineChart.invalidate();
        barChart.invalidate();


    }

    public String[] getMinutesCount() {
       // return new String[mData.getDatas().size()];
        return new String[240];
    }

    /*设置量表对齐*/
    private void setOffset() {
        float lineLeft = lineChart.getViewPortHandler().offsetLeft();
        float barLeft = barChart.getViewPortHandler().offsetLeft();
        float lineRight = lineChart.getViewPortHandler().offsetRight();
        float barRight = barChart.getViewPortHandler().offsetRight();
        float barBottom = barChart.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
        /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
            //offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            // barChart.setExtraLeftOffset(offsetLeft);
            transLeft = lineLeft;

        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            lineChart.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }

        /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
            //offsetRight = Utils.convertPixelsToDp(lineRight);
            //barChart.setExtraRightOffset(offsetRight);
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            lineChart.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        barChart.setViewPortOffsets(transLeft, 5, transRight, barBottom);
    }

}
