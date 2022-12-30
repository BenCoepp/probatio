package de.bencoepp.entity.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;

public class TestResult {
    private String title;
    private Integer testId;
    private String info;
    private Boolean successful;
    private ArrayList<StepResult> stepResults;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public ArrayList<StepResult> getStepResults() {
        return stepResults;
    }

    public void setStepResults(ArrayList<StepResult> stepResults) {
        this.stepResults = stepResults;
    }

    public void fromJson(String json) throws JsonProcessingException {
        this.stepResults = new ArrayList<>();
        this.title = JsonPath.read(json, "$.result.title");
        this.testId = JsonPath.read(json, "$.result.testId");
        this.info = JsonPath.read(json, "$.result.info");
        this.successful = JsonPath.read(json, "$.result.successful");
        int countResult = JsonPath.read(json, "$.result.stepResults.length()");
        for (int i = 0; i < countResult; i++) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = mapper.writeValueAsString(JsonPath.read(json, "$.result.stepResults.[" + i + "]"));
            StepResult stepResult = new StepResult();
            stepResult.fromJson("{\"result\":" + jsonObject + "}");
            this.stepResults.add(stepResult);
        }
    }
}
