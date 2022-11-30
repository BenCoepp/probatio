package de.bencoepp.utils.asciichart.chart;


import de.bencoepp.utils.asciichart.chart.entity.LineElement;

import java.util.ArrayList;
import java.util.Arrays;

public class LineChart extends Chart{
    private ArrayList<LineElement> elements;
    private Boolean showTimeline;
    private Boolean showValues;

    @Override
    public String render() {
        String[][] results = new String[15][100];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 100; j++) {
                results[i][j] = "█";
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            String str = "";
            for (int j = 0; j < 100; j++) {
                str += results[i][j];
            }
            stringBuilder.append(str);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public ArrayList<LineElement> getElements() {
        return elements;
    }

    public void setElements(ArrayList<LineElement> elements) {
        this.elements = elements;
    }

    public Boolean getShowTimeline() {
        return showTimeline;
    }

    public void setShowTimeline(Boolean showTimeline) {
        this.showTimeline = showTimeline;
    }

    public Boolean getShowValues() {
        return showValues;
    }

    public void setShowValues(Boolean showValues) {
        this.showValues = showValues;
    }
}
