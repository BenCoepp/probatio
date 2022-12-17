package de.bencoepp.utils.asciichart.chart;

public class Chart {
    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**Use this method to render the chart as a string and return it. The actual rendering will be done
     * by the chart itself.
     * @return chartString
     */
    public String render() {
        return null;
    }

    public String[] getLinesAsString(){
        return null;
    }
}
