package com.bhw;


//import com.bhw.JDBCUnit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class WordCount {
    public TreeMap<String, Integer> displayWordCount(String fileName) {
        //以字符流的形式统计
        TreeMap<String, Integer> tm = new TreeMap<String, Integer>();
        try {
            //读取文件
            FileReader fileReader = new FileReader(fileName);
            //使用流的方式读取内容
            BufferedReader reader = new BufferedReader(fileReader);
            //使用TreeMap，它会自动将结果按照字典的顺序排序

            String readLine = null;
            while((readLine = reader.readLine()) != null){
                //将字母排序为小写
                readLine = readLine.toLowerCase();
                //过滤出只含有字母的字段
                String[] str = readLine.split("[\\s]+");
                //过滤掉所有的空格,“+”代表多个的意思。
                for (int i = 0; i < str.length; i++) {//循环统计出现次数
                    String word = str[i].trim();
                    if (tm.containsKey(word)) {
                        tm.put(word, tm.get(word) + 1);
                    } else {
                        tm.put(word, 1);
                    }
                }
            }

            //输出我们想要的字符串格式
           // System.out.println("按字典序输出为：");
            Iterator<Map.Entry<String, Integer>> it = tm.entrySet().iterator();
            //使用迭代器取值
           // while(it.hasNext()) {
              //  Map.Entry<String, Integer> entry = it.next();
                //将结果输出到控制台
               // System.out.println(entry.getKey() + "\t" + entry.getValue());
            //}
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tm;
    }
}