package demo.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * 将文件进行合并规整
 */
public class Raw {

    private static String file1="D:\\BigData_project\\demo\\src\\main\\resources\\stopwords-master\\baidu_stopwords.txt";
    private static String file2="D:\\BigData_project\\demo\\src\\main\\resources\\stopwords-master\\cn_stopwords.txt";
    private static String file3="D:\\BigData_project\\demo\\src\\main\\resources\\stopwords-master\\hit_stopwords.txt";
    private static String file4="D:\\BigData_project\\demo\\src\\main\\resources\\stopwords-master\\scu_stopwords.txt";

    private static String result="D:\\BigData_project\\demo\\src\\main\\resources\\stopwords-master\\Result.txt";

    public static void main(String[] args) {
        // 读取文件
        Set<String> set1 = readWordsFromFile(file1);
        Set<String> set2 = readWordsFromFile(file2);
        Set<String> set3 = readWordsFromFile(file3);
        Set<String> set4 = readWordsFromFile(file4);

        // 合并两个Set
        Set<String> set = new HashSet<>(set1);
        set.addAll(set2);
        set.addAll(set3);
        set.addAll(set4);
        // 输出到文件
        writeWordsToFile(result, set);
    }

    // 从文件中读取词语并存储到Set中
    public static Set<String> readWordsFromFile(String filename) {
        Set<String> set = new HashSet<>();
        try {
            File file = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                // 根据实际情况对每行数据进行处理
                String[] words = line.split("\t");

                for (String word : words) {
                    set.add(word);
                }

            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    // 将Set中的词语写入文件
    public static void writeWordsToFile(String filename, Set<String> set) {
        try {
            File file = new File(filename);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String word : set) {
                writer.write(word);
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
