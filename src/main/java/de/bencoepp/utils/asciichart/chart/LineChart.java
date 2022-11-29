package de.bencoepp.utils.asciichart.chart;


import de.bencoepp.utils.asciichart.chart.entity.LineElement;

import java.util.ArrayList;

public class LineChart extends Chart{
    private ArrayList<LineElement> elements;
    private Boolean showTimeline;
    private Boolean showValues;

    @Override
    public String render() {
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder.toString();
    }
}
