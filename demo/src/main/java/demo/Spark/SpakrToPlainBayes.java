package demo.Spark;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Data
@ToString
@Slf4j
@Component
public class SpakrToPlainBayes {

    /**
     * 分类标签号和问句模板对应表
     */
    Map<Double, String> questionsPattern;

    /**
     * Spark贝叶斯分类器
     */
    NaiveBayesModel myModel;

    /**
     * 词汇表
     */
    Map<String, Integer> vocabulary;

    /**
     * 关键字与其词性的map键值对集合 == 句子抽象
     */
    Map<String, String> abstractMap;

    /**
     * 问题及字典模板所在的根目录
     */
    public String path = "/opt/HanLP/data";
 //   public String path = "D:/BigData_project/demo/src/main/resources/HanLP/data";

    int modelIndex = 0;

    public SpakrToPlainBayes() throws Exception {
        this.path = path + '/';
        questionsPattern = loadQuestionsPattern();
        vocabulary = loadVocabulary();
        myModel = loadClassifierModel();
    }

    /**
     * 加载词典 匹配分词后的单词
     *
     * @return 词典
     */
    public Map<String, Integer> loadVocabulary() {
        Map<String, Integer> vocabulary = new HashMap<>();
        File file = new File(path + "question/vocabulary.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] array = line.split(":");
                int index = Integer.parseInt(array[0]);
                String word = array[1];
                vocabulary.put(word, index);
            }
        } catch (Exception ignored) {

        }
        return vocabulary;
    }

    /**
     * 加载文件，并读取内容返回
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public String loadFile(String filename) throws IOException {
        File file = new File(path + filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line).append("`");
        }
        br.close();
        return content.toString();
    }

    /**
     * 句子分词后与词汇表进行key匹配转换为double向量数组
     *
     * @param sentence
     * @return double[]
     * @throws Exception
     */
    public double[] sentenceToArrays(String sentence) {
        double[] vector = new double[vocabulary.size()];

        Segment segment = HanLP.newSegment();
        List<Term> terms = segment.seg(sentence);
        for (Term term : terms) {
            String word = term.word;
            if (vocabulary.containsKey(word)) {
                int index = vocabulary.get(word);
                vector[index] = 1;
            }
        }
        return vector;
    }

    /**
     * Spark朴素贝叶斯(naiveBayes)
     * 对特定的模板进行加载并分类
     *
     * @return
     * @throws Exception
     */
    public NaiveBayesModel loadClassifierModel() throws Exception {
        //设置spark环境
        SparkConf conf = new SparkConf().setAppName("NaiveBayes").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<LabeledPoint> train_list = new ArrayList<>();

        String[] sentences = null;

        /**
         * 百日咳的症状是什么
         */
        String symptomQuestion = loadFile("question/0.txt");
        sentences = symptomQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(0.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的病因是什么
         */
        String caseQuestion = loadFile("question/1.txt");
        sentences = caseQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(1.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的预防措施是什么
         */
        String preventQuestion = loadFile("question/2.txt");
        sentences = preventQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(2.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的治愈时间是什么
         */
        String cure_lastTimeQuestion = loadFile("question/3.txt");
        sentences = cure_lastTimeQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(3.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的治愈概率是什么
         */
        String cure_probQuestion = loadFile("question/4.txt");
        sentences = cure_probQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(4.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的治愈方法是什么
         */
        String cure_wayQuestion = loadFile("question/5.txt");
        sentences = cure_wayQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(5.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的易感人群是什么
         */
        String easy_getQuestion = loadFile("question/6.txt");
        sentences = easy_getQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(6.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的描述是什么
         */
        String descQuestion = loadFile("question/7.txt");
        sentences = descQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(7.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的忌口是什么
         */
        String no_eatQuestion = loadFile("question/8.txt");
        sentences = no_eatQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(8.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的适合吃是什么
         */
        String do_eatQuestion = loadFile("question/9.txt");
        sentences = do_eatQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(9.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的推荐药品是什么
         */
        String common_drugQuestion = loadFile("question/10.txt");
        sentences = common_drugQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(10.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的检查是什么
         */
        String need_checkQuestion = loadFile("question/11.txt");
        sentences = need_checkQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(11.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的并发症是什么
         */
        String acompany_withQuestion = loadFile("question/12.txt");
        sentences = acompany_withQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(12.0, Vectors.dense(array));
            train_list.add(train_one);
        }
        /**
         * 百日咳的症状可能的病是什么
         */
        String has_symptomQuestion = loadFile("question/13.txt");
        sentences = has_symptomQuestion.split("`");
        for (String sentence : sentences) {
            double[] array = sentenceToArrays(sentence);
            LabeledPoint train_one = new LabeledPoint(13.0, Vectors.dense(array));
            train_list.add(train_one);
        }

        JavaRDD<LabeledPoint> trainingRDD = sc.parallelize(train_list);
        NaiveBayesModel model = NaiveBayes.train(trainingRDD.rdd());

        sc.close();

        return model;
    }

    /**
     * 加载问题模板 == 分类器标签
     *
     * @return
     */
    public Map<Double, String> loadQuestionsPattern() {
        HashMap<Double, String> questionsPattern = new HashMap<>();
        File file = new File(path + "question/question_classification.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                double index = Double.parseDouble(tokens[0]);
                String pattern = tokens[1];
                questionsPattern.put(index, pattern);
            }
        } catch (Exception ignored) {

        }
        return questionsPattern;
    }

    /**
     * 抽象句子，利用HanPL分词，将关键字进行词性抽象
     *
     * @param originalSentence  原始的句子
     * @return abstractQuery 抽象的句子
     */
    public String queryAbstract(String originalSentence) {
        boolean flag=false;
        Segment segment = HanLP.newSegment().enableCustomDictionary(true);
        List<Term> terms = segment.seg(originalSentence);
        StringBuilder abstractQuery = new StringBuilder();
        abstractMap = new HashMap<>();
        for (Term term : terms) {

            String word = term.word;
            String termStr = term.toString();
            //dz和sym两种分法
            if (termStr.contains("dz")) {
                flag=true;
                abstractQuery.append("dz");
                abstractMap.put("dz", word);
            } else if (termStr.contains("sym")) {
                flag=true;
                abstractQuery.append("sym");
                abstractMap.put("sym", word);
            } else {
                abstractQuery.append(word).append(" ");
            }

            //打出关键词
            if(flag){
                log.info("关键词:"+term);
                flag=false;
            }

        }
        return abstractQuery.toString();
    }

    /**
     * 贝叶斯分类器分类的结果，拿到匹配的分类标签号，并根据标签号返回问题的模板
     * @param abstractQuery 抽象的句子
     * @return questionsPattern 问题模板
     * @throws Exception
     */
    public String queryClassify(String abstractQuery) throws Exception {
        double[] testArray = sentenceToArrays(abstractQuery);
        Vector v = Vectors.dense(testArray);
        int flag=0;


        double index = myModel.predict(v);
        modelIndex = (int)index;
        log.info("the model index is " + index);
        Vector vRes = myModel.predictProbabilities(v);
        for (int i = 0; i < vRes.size(); i++) {
            double apply = vRes.apply(i);
            if(apply<0.1){
                flag++;
            }
            //log.info("匹配概率:"+apply);
        }
        if(flag==14)
        {
            modelIndex=999;
            log.info("这句话是废话.不做处理!");
            return "dz 废话";
        }
        return questionsPattern.get(index);
    }

    /**
     * 模板还原成句子
     * @param questionsPattern 问题模板
     * @return
     */
    public String queryExtenstion(String questionsPattern) {
        Set<String> keys = abstractMap.keySet();
        for (String key : keys) {
            if (questionsPattern.contains(key)) {
                String value = abstractMap.get(key);
                questionsPattern = questionsPattern.replace(key, value);
            }
        }
        String finalQuery = questionsPattern;
        /**
         * 当前句子处理完，抽象map清空释放空间并置空，等待下一个句子的处理
         */
        abstractMap.clear();
        abstractMap = null;
        return finalQuery;
    }

    /**
     * 处理方法
     * @param originalSentence
     * @return
     * @throws Exception
     */
    public ArrayList<String> analysisQuery(String originalSentence) throws Exception {
        String abstractQuery = queryAbstract(originalSentence);
        String questionsPattern = queryClassify(abstractQuery);
        String finalQuery = queryExtenstion(questionsPattern);
        String[] finalArray = finalQuery.split(" ");
        ArrayList<String> res = new ArrayList<>();
        res.add(String.valueOf(modelIndex));
        Collections.addAll(res, finalArray);
        return res;
    }

}