package xyz.ConstruTec.app.model;

import java.util.List;

public class ChartData {
    private List<String> labels;
    private List<Dataset> datasets;
    
    public static class Dataset {
        private String label;
        private List<Number> data;
        private String backgroundColor;
        private String borderColor;
        private int borderWidth;
        private boolean fill;
        
        public Dataset() {
            this.borderWidth = 1;
            this.fill = false;
        }
        
        public String getLabel() {
            return label;
        }
        public void setLabel(String label) {
            this.label = label;
        }
        public List<Number> getData() {
            return data;
        }
        public void setData(List<Number> data) {
            this.data = data;
        }
        public String getBackgroundColor() {
            return backgroundColor;
        }
        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }
        public String getBorderColor() {
            return borderColor;
        }
        public void setBorderColor(String borderColor) {
            this.borderColor = borderColor;
        }
        public int getBorderWidth() {
            return borderWidth;
        }
        public void setBorderWidth(int borderWidth) {
            this.borderWidth = borderWidth;
        }
        public boolean isFill() {
            return fill;
        }
        public void setFill(boolean fill) {
            this.fill = fill;
        }
    }
    
    public ChartData() {
    }
    
    public List<String> getLabels() {
        return labels;
    }
    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
    public List<Dataset> getDatasets() {
        return datasets;
    }
    public void setDatasets(List<Dataset> datasets) {
        this.datasets = datasets;
    }
} 