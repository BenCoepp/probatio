package de.bencoepp.utils.asciichart.chart;

import de.bencoepp.utils.asciichart.chart.entity.BarElement;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/** A Bar Chart can be configured in a few ways.
 * Please make sure to keep in mind that you need to use the
 * render method to render the chart.
 *
 */
public class BarChart extends Chart{
    private ArrayList<BarElement> elements;

    @Override
    public String render() {
        if(elements.isEmpty()){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        int maxValue = getLargestValue().intValue();
        int increment = maxValue / 25;
        int longestLabelLength = getLargestLabelLeangth();
        for (BarElement element : elements) {
            stringBuilder.append(" ".repeat(Math.max(0, longestLabelLength - element.getTitle().length())));
            stringBuilder.append(element.getTitle()).append(" ▏");
            String str = "";
            if(element.getValue().toString().length() < String.valueOf(maxValue).length()){
                for (int i = 0; i < String.valueOf(maxValue).length()-element.getValue().toString().length(); i++) {
                    str += " ";
                }
            }
            stringBuilder.append("  ").append(str).append(element.getValue()).append("  ");
            String bar = "█";
            stringBuilder.append(bar.repeat(Math.max(0, element.getValue().intValue())));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private BigDecimal getLargestValue(){
        BigDecimal max = new BigDecimal(BigInteger.ZERO);
        for (BarElement element : elements) {
            if(element.getValue().compareTo(max) == 1){
                max = element.getValue();
            }
        }
        return max;
    }

    private int getLargestLabelLeangth(){
        int max = 0;
        for (BarElement element : elements) {
            if(element.getTitle().length() > max){
                max = element.getTitle().length();
            }
        }
        return max;
    }

    @Override
    public String[] getLinesAsString() {
        String out = render();
        return out.split("\n");
    }

    public ArrayList<BarElement> getElements() {
        return elements;
    }

    public void setElements(ArrayList<BarElement> elements) {
        this.elements = elements;
    }
}
