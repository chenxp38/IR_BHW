package com.bhw;
import com.bhw.Read_data.*;
import java.util.*;

public class Caculate_Rank {
    public static void main(String[] args) throws Exception {
        System.out.println("输入查询词：~");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        System.out.println("查询\""+s+"\"中……");
        Read_data rd = new Read_data();
        Map<String, Double> file_tfidf = rd.calculate_rank(s);
        //System.out.println("hhhhh");

        System.out.println("输出检索结果：");
        System.out.print("文件Id    ");
        System.out.println("tf_idf值");
        Map<String, Double> pageRank = sort(file_tfidf);
        Iterator<Map.Entry<String, Double>> it = pageRank.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, Double> entry = it.next();
            System.out.print(entry.getKey() + "    ");
            System.out.println(entry.getValue());

        }
    }

    public static Map<String, Double> sort(Map<String, Double> unsortedMap) {
        //先存在数组里面，排完序再存进map
        //System.out.println(unsortedMap);
        Map<String, Double> result = new LinkedHashMap<String, Double>();
        int len = unsortedMap.size();
        String [] keystring = new String [len];
        Double [] value = new Double [len];

        //System.out.println(unsortedMap);

        Iterator<Map.Entry<String, Double>> it = unsortedMap.entrySet().iterator();
        int index = 0;
        while(it.hasNext()) {
            Map.Entry<String, Double> entry = it.next();
            String key_t = entry.getKey();
            Double value_t = entry.getValue();
            keystring[index] = entry.getKey();
            value[index] = entry.getValue();
            index++;
        }
        index = 0;

        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                if (value[i] < value[j]) {
                    Double temp = value[i];
                    String temp_s = keystring[i];

                    value[i] = value[j];
                    keystring[i] = keystring[j];
                    value[j] = temp;
                    keystring[j] = temp_s;
                }
            }
        }
        for (int i = 0; i < len; i++) {
            //System.out.print(keystring[i] + " ");
            //System.out.println(value[i]);
            result.put(keystring[i],value[i]);
        }


        return result;
    }
}
