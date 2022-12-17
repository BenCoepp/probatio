package de.bencoepp.utils.asciichart.chart.entity;

import java.math.BigDecimal;

public class BarElement {
    private String title;
    private String description;
    private BigDecimal value;

    public BarElement(String title, String description, BigDecimal value) {
        this.title = title;
        this.description = description;
        this.value = value;
    }

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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
