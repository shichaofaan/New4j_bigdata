package demo.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import demo.Entity.Neo4j.Disease;
import demo.Entity.Mysql.User;
import demo.Enum.CheckUserEnum;
import demo.Mapper.UserMapper;
import demo.Spark.SpakrToPlainBayes;
import demo.service.Neo4j.DiseaseRepository;
import demo.service.Userservice;
import demo.utils.RedisUtil;
import demo.utils.SimilarityAlgorithm;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;


@SpringBootTest
@Slf4j
class DemoApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private SimilarityAlgorithm similarityAlgorithm;

    @Autowired
    private Userservice userservice;

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private SpakrToPlainBayes spakrToPlainBayes;

    @Autowired
    private RedisUtil redisUtil;

    //获取疾病字典进行去掉没用信息
    private String diseasepath="D:\\BigData_project\\demo\\src\\main\\resources\\HanLP\\data\\dictionary\\custom\\disease.txt";

    //获取症状字典去掉没用信息
    private String Symptomspath="D:\\BigData_project\\demo\\src\\main\\resources\\HanLP\\data\\dictionary\\custom\\Symptoms.txt";

    private String[] days={"一","二","三","四","五","六","七"};

    /*@Test
    void mysql() {

        User user=userMapper.selectById(0);
        user.setUser_id(0);

        User user2=userMapper.selectById(0);
        user2.setUser_id(0);

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //userMapper.insert(user);
        userMapper.updateById(user2);

        userMapper.updateById(user);
    }*/

    /*
     *分页查询
     */
    @Test
    void select(){
        Page<User> page=new Page<>(1,5);
        userMapper.selectPage(page,null);
        page.getRecords().forEach(System.out::println);
    }

    @Test
    void delete(){
        //User user = userMapper.selectById("0");
        userMapper.deleteById(169948062339301376L);
    }

    @Test
    void insert(){
        User user=new User();

        user.setUserEmil("2835717189@qq.com")
                .setUserName("xxx-leader")
                .setUserPassword("123456")
                .setUserNumber("2835717189");

        userMapper.insert(user);
    }

    @Test
    void selectone(){
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("user_number",57387741);
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
        //User user1= userMapper.selectById(1L);
        //System.out.println("id:"+user1.getUserId()+"   字符串:"+user1);
        //System.out.println(userMapper.selectById(1));
        //System.out.println(user1);
        //System.out.println(user);
    }

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    void select_emil_number(){

       // User user = userserviceimpl.SelectUserbyemil( "573877411@qq.com");
       // User user1 = userservice.SelectUserbyemil("573877411@qq.com");
       // System.out.println(user1);

    }

    @Test
    void update_emil_number(){

        // User user = userserviceimpl.SelectUserbyemil( "573877411@qq.com");
        User user=new User();

        user.setUserEmil("2835717189@qq.com")
                .setUserName("xxx-leader")
                .setUserPassword("123456")
                .setUserNumber("283571717");

        boolean b = userservice.UpdateUserbyemail(user);
        System.out.println(b);

    }


    @Test
    void test_nume(){
        //System.out.println(CheckUserEnum.Right);
        CheckUserEnum[] values = CheckUserEnum.values();
        for (CheckUserEnum value : values) {
            System.out.println(value);
        }
    }


    @Test
    void stringtofloat() {
        float number=Float.valueOf("0.05");

        System.out.println(number);
    }


    @Test
    void personRepository(){

        Optional<Disease> all = diseaseRepository.findById(1L);

        Disease disease = all.get();

        String desc = disease.getDesc();

        List<String> cure_department = disease.getCure_department();

        for (String s : cure_department) {
            log.info("部门:"+s);
        }

        log.info(disease.getName()+"描述:"+desc);
    }

    @Test
    void findBytitle(){

        //String s = diseaseRepository.DiseaseTocause("肺念珠菌病");

        //log.info("病因:"+s);

        //String s1 = diseaseRepository.DiseaseToPrevent("肺念珠菌病");

        //log.info("预防措施:"+s1);

        //String[] s = diseaseRepository.DiseaseToSympto("肥胖");

       // for (String s1 : s) {
        //    log.info("疾病是:"+s1);
       // }

        String strings = diseaseRepository.DiseaseToPrevent("肺念珠菌病");

        log.info(strings);

        //List<Disease> byName = diseaseRepository.findByName(5L);

        //for (Disease disease : byName) {
        //    log.info(disease.getName());
        //}

    }

    @Test
    void maintest(){

        /**
         * 本地模式，*表示启用多个线程并行计算
         */
        SparkConf conf = new SparkConf().setAppName("NaiveBayesTest").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        sc.setLogLevel("STDOUT");

        /**
         * MLlib的本地向量主要分为两种，DenseVector和SparseVector
         * 前者是用来保存稠密向量，后者是用来保存稀疏向量
         */

        /**
         * 两种方式分别创建向量  == 其实创建稀疏向量的方式有两种，本文只讲一种
         * (1.0, 0.0, 1.0, 0.0, 1.0, 0.0）
         * (1.0, 1.0, 1.0, 1.0, 0.0, 1.0）
         */

        //稠密向量 == 连续的
        Vector vMale = Vectors.dense(1,0,1,0,1,0);


        //稀疏向量 == 间隔的、指定的，未指定位置的向量值默认 = 0.0
        int len = 6;
        int[] index = new int[]{0,1,2,3,5};
        double[] values = new double[]{1,1,1,1,1};
        //索引0、1、2、3、5位置上的向量值=1，索引4没给出，默认0
        Vector vFemale = Vectors.sparse(len, index, values);
        //System.err.println("vFemale == "+vFemale);
        /**
         * labeled point 是一个局部向量，要么是密集型的要么是稀疏型的
         * 用一个label/response进行关联
         * 在MLlib里，labeled points 被用来监督学习算法
         * 我们使用一个double数来存储一个label，因此我们能够使用labeled points进行回归和分类
         * 在二进制分类里，一个label可以是 0（负数）或者 1（正数）
         * 在多级分类中，labels可以是class的索引，从0开始：0,1,2,......
         */

        //训练集生成 ，规定数据结构为LabeledPoint == 构建方式:稠密向量模式  ，1.0:类别编号 == 男性
        LabeledPoint train_one = new LabeledPoint(1.0,vMale);  //(1.0, 0.0, 1.0, 0.0, 1.0, 0.0）
        //训练集生成 ，规定数据结构为LabeledPoint == 构建方式:稀疏向量模式  ，2.0:类别编号 == 女性
        LabeledPoint train_two = new LabeledPoint(2.0,vFemale); //(1.0, 1.0, 1.0, 1.0, 0.0, 1.0）
        //我们也可以给同一个类别增加多个训练集
        LabeledPoint train_three = new LabeledPoint(2.0,Vectors.dense(0,1,1,1,0,1));

        //List存放训练集【三个训练样本数据】
        List<LabeledPoint> trains = new ArrayList<>();
        trains.add(train_one);
        trains.add(train_two);
        trains.add(train_three);

        /**
         * SPARK的核心是RDD(弹性分布式数据集)
         * Spark是Scala写的,JavaRDD就是Spark为Java写的一套API
         * JavaSparkContext sc = new JavaSparkContext(sparkConf);    //对应JavaRDD
         * SparkContext	    sc = new SparkContext(sparkConf)    ;    //对应RDD
         * 数据类型为LabeledPoint
         */
        JavaRDD<LabeledPoint> trainingRDD = sc.parallelize(trains);

        /**
         * 利用Spark进行数据分析时，数据一般要转化为RDD
         * JavaRDD转Spark的RDD
         */
        NaiveBayesModel nb_model = NaiveBayes.train(trainingRDD.rdd());

        //测试集生成  == 以下的向量表示，这个人具有特征：短发（1），运动鞋（3）
        double []  dTest = {1,0,1,0,0,0};
        Vector vTest =  Vectors.dense(dTest);//测试对象为单个vector，或者是RDD化后的vector

        //朴素贝叶斯用法
        int modelIndex =(int) nb_model.predict(vTest);
        System.out.println("标签分类编号："+modelIndex);// 分类结果 == 返回分类的标签值
        /**
         * 计算测试目标向量与训练样本数据集里面对应的各个分类标签匹配的概率结果
         */
        System.out.println(nb_model.predictProbabilities(vTest));
        if(modelIndex == 1){
            System.out.println("答案：贝叶斯分类器推断这个人的性别是男性");
        }else if(modelIndex == 2){
            System.out.println("答案：贝叶斯分类器推断这个人的性别是女性");
        }
        //最后不要忘了释放资源
        sc.close();

    }

    @Test
    @SneakyThrows
    void xxx(){

        ArrayList<String> strings = spakrToPlainBayes.analysisQuery("心脏病的易感人群");

        for (String string : strings) {
            log.info(string);
        }

    }

    @Test
    void test(){

        String s = GetNo3(3, 3);

        log.info(s);

    }

    /**
     * 求前day天len大热门的疾病
     * @param day
     * @param len
     * @return
     */
    public String GetNo3(int day,int len){

        //利用hashmap进行数量叠加
        HashMap<String, Integer> hashMap=new HashMap<>();

        //两个set进行去重
        Set<String> diseaseSet = readWordsFromFile(diseasepath);

        Set<String> SymptomsSet = readWordsFromFile(Symptomspath);

        //循环数组
        int i,j,k;

        //取三天的的信息
        for (i=0;i<day;i++)
        {
            //获取每天的用户提问
            Map<Object, Object> o = redisUtil.hmget(getDate(i) + "Partner:");
            //利用,分割每个问题
            String[] split = o.toString().split(",");
            //这是每一个用户的提问
            for (String s : split) {
                //log.info(s+"");
                //利用=分割出key和value
                String[] split1 = s.split("=");
                if(split1.length>1)
                {
                    //第二个元素是问题
                    String s1 = split1[1];
                    //存入hashmap进行去重
                    Integer put = hashMap.get(s1);
                    //判断是否为正常需要词语
                    if(diseaseSet.contains(s1)||SymptomsSet.contains(s1)){
                        //如果本来就有就叠加没有就给1
                        if(put!=null){
                            put++;
                            hashMap.put(s1,put);
                        }else{
                            hashMap.put(s1,1);
                        }
                    }
                }
            }
        }

        //输出hashmap
        for (Map.Entry<String, Integer> stringIntegerEntry : hashMap.entrySet()) {
            log.info("Key = " + stringIntegerEntry.getKey() + ", Value = " + stringIntegerEntry.getValue());
        }

        //创造len个位置数组
        int[] lenday=new int[len];
        String[] answer=new String[len];
        boolean flag=true;

        //设置元素提取里面的前len大信息
        for(i=0;i<len;i++)
        {
            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                Integer value = entry.getValue();
                String key = entry.getKey();

                //首先这个答案不能再数组里面存在
                for(j=0;j<len;j++)
                {
                    if(answer[j]!=null&&answer[j].contains(key))
                    {
                        flag=false;
                    }
                }

                //其次看value值
                if(flag){
                    for(j=0;j<len;j++)
                    {
                        //如果这个值之前存在
                        if(i!=j&&value==lenday[j])
                        {
                            flag=false;
                        }
                    }
                }

                //如果都满足就赋值
                if(flag&&lenday[i]<value){
                    lenday[i]=value;
                    answer[i]=key;
                }

                flag=true;
            }
        }

        //获取字符
        String daychinese = days[day-1];

        String result="近"+daychinese+"天的"+len+"个热门问题";

        for (i = 0; i < len; i++) {
            result=result.concat("'"+answer[i]+"'被提问了"+lenday[i]+"次.");
        }

        result=result.concat("------注意预防这些病毒!");

        return result;
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

    /**
     * 得到时间
     * @return
     */
    private String getDate(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    /**
     * 得到i天前的信息时间
     * @return
     */
    private String getDate(long i){
        long daytime=86400000;
        i=i*daytime;
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis()-i);
        return formatter.format(date);
    }
}
