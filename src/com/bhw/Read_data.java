package com.bhw;

import java.io.File;
import java.io.FileFilter;
import java.lang.management.PlatformLoggingMXBean;
import java.util.*;

import org.apache.tika.Tika;
import com.bhw.VariousAnalyzer.*;
import com.bhw.WordCount.*;

import com.bhw.WordCount.*;

import static java.lang.Math.log;

public class Read_data {
    private double tf;
    private double idf;
    private double tf_idf;

    public double getTf() {
        return tf;
    }

    public double getIdf() {
        return idf;
    }

    public double getTf_idf() {
        return tf_idf;
    }

    public void setTf(double tf) {
        this.tf = tf;
    }

    public void setIdf(double idf) {
        this.idf = idf;
    }

    public void setTf_idf(double tf_idf) {
        this.tf_idf = tf_idf;
    }

    public Map<String, Double> calculate_rank(String keys) throws Exception {
        String path = "D:/Learning_cxp/IR_resource";		//要遍历的路径
        File file1 = new File(path);		//获取其file对象
        List<String> allpath = getAllFile(file1); //list的下标就是文档的index
        int len = allpath.size();
        int allfile = len; //总文件数
        int hasfile = 0; //包含检索词的文件数

        Map<String, Double> file_tfidf = new LinkedHashMap<String, Double>();       //数据采用的顺序结构,第一列文件名，第二列tfidf

        for (int i = 0; i < len; i++) {
            int allword = 0; //文件总词数
            int key_times = 0; //检索词出现次数

            TreeMap<String, Integer> tm = new TreeMap<String, Integer>();
            String filename = allpath.get(i);
            File file = new File(filename);
            Tika tika = new Tika();
            String content = tika.parseToString(file);
            content = new String(content.getBytes("ISO-8859-1"), "gbk");
            //System.out.println("文件内容为:\n" + content);
            VariousAnalyzer variousAnalyzer = new VariousAnalyzer();
            variousAnalyzer.IkAnalyzer(content);
            WordCount wc = new WordCount();
            tm = wc.displayWordCount(filename);

            //计算tf，idf等
            //单个文档
            //System.out.println("按字典序输出为：");
            Iterator<Map.Entry<String, Integer>> it = tm.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<String, Integer> entry = it.next();
                //将结果输出到控制台
                //System.out.println(entry.getKey() + "\t" + entry.getValue());
                allword += entry.getValue(); //计算总词数
                if (entry.getKey().equals(keys)) {
                    hasfile++; //该文档含有该检索词，含检索词文档数加一
                    key_times += entry.getValue(); //该文档含检索词数
                }

            }
            if (allword != 0){
                this.tf = Double.valueOf(key_times) / Double.valueOf(allword);
                file_tfidf.put(allpath.get(i), this.tf); //这时第二列存的是tf，还不是tf_idf
            }

        }
        //输出文件的tf，来测试有没有算对
        //System.out.println(file_tfidf);//正确
        //java中对数以e为底
        //换底公式：logx(y) =loge(y) / loge(x)，我们令x = 10，以10为底计算idf
        // log10(allfile / hasfile)
        //这里加一是为了防止hasfile为0
        this.idf = (log(Double.valueOf(allfile) / Double.valueOf(hasfile + 1))) / log(10);
        //tf_idf(t, d) = tf(t, d) * idf (term)

        //重新放一个相同的key,会自动覆盖value的
        //1.使用entrySet()遍历
        //System.out.println("使用entrySet()遍历");

        Iterator<Map.Entry<String, Double>> it = file_tfidf.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, Double> entry = it.next();
            String key_t = entry.getKey();
            Double value_t = entry.getValue();
            this.tf_idf = this.idf * value_t;
            file_tfidf.put(key_t, this.tf_idf); //这时第二列存的是tf_idf
        }
        //System.out.println(file_tfidf);
        return file_tfidf;
    }

    public List<String> getAllFile(File file) {
        File[] fs = file.listFiles();
        List<String> allpath = new ArrayList<>();
        for(File f:fs){
            if(f.isDirectory())	//若是目录，则递归打印该目录下的文件
                getAllFile(f);
            if(f.isFile()) {        //若是文件，直接打印
                allpath.add(f.toString());
                System.out.println(f);
            }
        }
        return allpath;
    }
}