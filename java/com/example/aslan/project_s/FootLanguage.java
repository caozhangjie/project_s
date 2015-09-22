package com.example.aslan.project_s;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caozj on 9/14/15.
 */
public class FootLanguage {

    private List<double []> data_stream;
    private List<double []> start_flag;
    private List<List<double []> > foot_language;
    private List<double []> end_flag;
    private boolean is_start;
    private boolean is_end;
    private int number_find;
    private int len;
    private double threshold;
    private double fmin;
    private double fmax;


    FootLanguage(){
        data_stream = new ArrayList<double[]>();
        foot_language = new ArrayList<List<double[]> >();
        is_start = false;
        is_end = false;
        number_find = -1;
        len = 0;
        threshold = 5;
        fmin = 0.9;
        fmax = 1.1;
    }

    public void setStrart(List<double []> new_lan){
        start_flag = new ArrayList<double[]>(new_lan);
    }

    public void setEnd(List<double []> new_lan){
        end_flag = new ArrayList<double[]>(new_lan);
    }

    public void setFootLanguage(List<double []> new_lan){
        foot_language.add(new_lan);
        len++;
    }

    public boolean match_lan(List<double[]> query, List<double[]> data){
        int width = query.size();
        int length = data.size();
        int select;
        double [][] x_matrix = new double[width + 1][length + 1];
        double [][] y_matrix = new double[width + 1][length + 1];
        double [][] distance_matrix = new double[width + 1][length + 1];
        double [][] v_matrix = new double[width + 1][length + 1];
        for(int i = 1; i < width; i ++){
            distance_matrix[i][0] = Double.POSITIVE_INFINITY;
        }
        distance_matrix[width][length] = -1;

        //初始化v_matrix
        for(int i = 0; i < width + 1; i++){
            for(int j = 0; j < length + 1; j++){
                v_matrix[i][j] = 1;
            }
        }

        for(int i = 1; i < width + 1; i++){
            for(int j = 1; j < length + 1; j++){
                select = compareThree(v_matrix[i - 1][j] * distance_matrix[i - 1][j], v_matrix[i - 1][j - 1] * distance_matrix[i - 1][j - 1], v_matrix[i][j - 1] * distance_matrix[i][j - 1]);
                distance_matrix[i][j] = distance_matrix[i - (select & 1)][j - ((select & 2) >> 1)] + distance_bt(query.get(i), data.get(j));
                x_matrix[i][j] = i - (select & 1);
                y_matrix[i][j] = j - ((select & 2) >> 1);
            }
        }
        if(distance_matrix[width][length] < threshold){
            return true;
        }
        else{
            return false;
        }
    }

    public int compareThree(double a, double b, double c){
        if(a < b){
            if(a < c){
                return 1;
            }
            else
            {
                return 2;
            }
        }
        else{
            if(c < b){
                return 2;
            }
            else
            {
                return 3;
            }
        }
    }

    public double distance_bt(double[] x, double y[]) {
        int len = x.length;
        double sum = 0;
        for(int i = 0; i < len; i++){
            sum += (x[i] - y[i]) * (x[i] - y[i]);
        }
        return sum;
    }

    public void calculate(double[] msg) {
        if(!is_start) {
            data_stream.remove(0);
            data_stream.add(msg);
            int len_data_stream = data_stream.size();
            for(int k = ((int) (len_data_stream - (start_flag.size() * fmax))); k < len_data_stream - (int)(start_flag.size() * fmin) && k > 0; k++) {
                if (match_lan(start_flag, data_stream.subList(k, len_data_stream))) {
                    is_start = true;
                    data_stream.clear();
                }
            }
        }
        else if(!is_end){
            data_stream.add(msg);
            int len_data_stream = data_stream.size();
            for(int k = ((int) (len_data_stream - (start_flag.size() * fmax))); k < len_data_stream - (int)(start_flag.size() * fmin) && k > 0; k++) {
                if (match_lan(end_flag, data_stream.subList(k, len_data_stream))) {
                    is_start = true;
                    data_stream = data_stream.subList(0, k);
                }
            }
        }
        else{
            for(int i = 0; i < len; i++){
                if(match_lan(data_stream, foot_language.get(i))){
                    number_find = i;
                    break;
                }
            }
            if(number_find != -1){
                //执行相应脚语
                number_find = -1;
            }
            else{
                //没找到脚语的操作
            }
        }
    }
}
