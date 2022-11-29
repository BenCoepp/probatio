package de.bencoepp.utils.asciichart;

import de.bencoepp.utils.asciichart.chart.Chart;

/** AsciiChart is the all in one solution to building and displaying charts within
 * a console environment. It can be used in other places as well, for instance in
 * displaying as String Data.
 *
 * To use Ascii Chart you need to do the following:
 * Chart chart = new Chart();
 * chart.addElements(..Data);
 * AsciiChart.render(chart);
 *
 */
public class AsciiChart {

    /**With this method you can render a chart that you have created. The chart
     * needs to have some elements as its data. If not you will receive a empty
     * string.
     *
     * @param chart
     * @return rendered Chart
     */
    public static String render(Chart chart){
        return chart.render();
    }
}
