package test.lxy.com.demo;

import android.util.SparseArray;

import com.github.mikephil.charting.components.XAxis;


public class MyXAxis extends XAxis {
    private SparseArray<String> labels;
    public SparseArray<String> getXLabels() {
        return labels;
    }
    public void setXLabels(SparseArray<String> labels) {
        this.labels = labels;
    }
}
