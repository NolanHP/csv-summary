package com.nolanhp;

public class ColumnStats{
    int count = 0;
    int nullCount = 0;

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;
    double sum;
    String name;
    boolean isNumeric = true;

    public void update(String value){
        count++;
        if(value == null || value.isBlank()){
            nullCount++;
            return;
        }
        try{
            Double n =Double.parseDouble(value);
            sum+=n;
            if(n > max) { max = n; }
            if(n < min) { min = n; }
        } catch (NumberFormatException e) {
            isNumeric = false;
        }
    }

    public double mean(){
        int nonNull = count - nullCount;
        if(nonNull == 0){
            return 0.0;
        }
        return sum/nonNull;
    }

    public double nullRate(){
        if(count!=0){
            return (double) nullCount/count;
        }
        return 0.0;
    }
}
