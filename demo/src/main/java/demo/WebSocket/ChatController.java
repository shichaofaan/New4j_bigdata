package demo.WebSocket;

import com.alibaba.fastjson.JSONObject;
import demo.Entity.Msg.UserMessage;
import demo.Spark.SparkUntil;
import demo.config.SparkUtilConfig;
import demo.config.SpringRedisConfig;
import demo.config.similarityAlgorithmConfig;
import demo.utils.RedisUtil;
import demo.utils.SimilarityAlgorithm;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//configurator必须声明要不然会产生空指针异常
@ServerEndpoint("/chatting/")
@Component
@Slf4j
public class ChatController {

    /*
    * 这里面的redis是单线程和wocket的多线程起了冲突
    * */
    @Resource
    private RedisUtil redisUtil = SpringRedisConfig.getBean(RedisUtil.class);

    /**
     * spark分析匹配
     */
    @Resource
    private SparkUntil sparkUntil = SparkUtilConfig.getBean(SparkUntil.class);

    /**
     * mongodb数据库使用
     */
    @Resource
    private SimilarityAlgorithm similarityAlgorithm = similarityAlgorithmConfig.getBean(SimilarityAlgorithm.class);

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //用来存储每个用户客户端对象的ChatEndpoint对象
    private final static Map<Session, String> onlineUsers = new ConcurrentHashMap<>();

    //获取疾病字典进行去掉没用信息
  //  private String diseasepath="D:\\BigData_project\\demo\\src\\main\\resources\\HanLP\\data\\dictionary\\custom\\disease.txt";
    private String diseasepath="/opt/HanLP/data/dictionary/custom/disease.txt";

    //获取症状字典去掉没用信息
  //   private String Symptomspath="D:\\BigData_project\\demo\\src\\main\\resources\\HanLP\\data\\dictionary\\custom\\Symptoms.txt";
     private String Symptomspath="/opt/HanLP/data/dictionary/custom/Symptoms.txt";

    //获取汉字数字
    private String[] days={"一","二","三","四","五","六","七"};

    //连接建立
    @OnOpen
    @SneakyThrows
    public void onOpen(Session session){

        log.info("已经有用户x:已经上线");

        //session和name保证uri唯一性
        String requestURI =session.getRequestURI().toString();

        log.info("url的tostring地址:"+requestURI);

        //这里的String准备后面存储用户内容用的，利用.区分
        onlineUsers.put(session,"");

        //获取名人名言
        String s = similarityAlgorithm.FamousMsg();

        session.getBasicRemote().sendText(s);

        //获取3天的前3大热门消息
        String Hotquestion = GetNo3(3, 3);

        session.getBasicRemote().sendText(Hotquestion);

        ChatController.addOnlineCount();
    }

    //收到消息
    @OnMessage
    @SneakyThrows
    public void onMessage(String message,Session session){

        ArrayList<String> strings = null;

        //session和name保证uri唯一性
        String requestURI =session.toString();

        String[] spliturl = requestURI.split("@");

        //直接拼接session的最后7为校验码，独一性，但是只能在一次对话框里面使用
        String surl = spliturl[spliturl.length-1];

        log.info("这是这次的唯一标识url:"+surl);

        //转换为json字符串
        UserMessage userMessage = JSONObject.parseObject(message, UserMessage.class);

        if (userMessage == null) {
            log.warn("监听到的消息为空或格式不正确，message:{}", message);
            return;
        }

        //获取前端传递来的name
        String username=userMessage.getName();
        String usermessage=userMessage.getMessage();
        String date=userMessage.getSendTime();

        //获取前端用户信息之后和session存入user信息
        String userofmsg = onlineUsers.get(session);

        //初始时间
        long begin = System.currentTimeMillis();

        //获取当前用户输入的答案
        String answer = sparkUntil.answer(usermessage);

        //获取当前用户输入的主要信息
        String MostInformation = TitleString(usermessage);

        log.info("存入的模板是:"+MostInformation);

        //如果这次的数据是有用的化进行存储即可
        if(!(answer.contains("这个语句我很陌生.我需要学习一下~~~")||answer.contains("dz"))){
            //如果可以在存入
            userofmsg=userofmsg.concat(MostInformation);
            //如果这次问题是正常的化存入数组里面
            onlineUsers.put(session,userofmsg);

            strings = sparkUntil.StepArray(userofmsg);
        }else{
            //上下文检测一下
            log.info("这是我们需要的主语:"+userofmsg);
            String umsg=TitleStringmsg(userofmsg,usermessage);
            log.info("这是我们拼接好的问句:"+umsg);
            answer = sparkUntil.answer(umsg+".这是第二次的msg.");

            strings = sparkUntil.StepArray(umsg);
        }

        //将回答和答案缓存到Redis
        MsgToRedis(surl,date,username,usermessage,answer);

        //发送消息的方法
        session.getBasicRemote().sendText(answer);

        //智能推荐系统

        String title = strings.get(0);

        if(title.contains("10")){
            //进行药物的推荐
            session.getBasicRemote().sendText("`");
        }else if(title.contains("11")||title.contains("12")||title.contains("13")){
            //进行医生的推荐
            session.getBasicRemote().sendText("&");
        }

        long end = System.currentTimeMillis();

        //将响应速度返回给前端
        session.getBasicRemote().sendText("*"+String.valueOf(end-begin));
    }

    /**
     * 做上下文联系的
     * @param userofmsg
     * @param usermessage
     * @return
     */
    private String TitleStringmsg(String userofmsg, String usermessage) {

        //对以往的句子进行分析
        String[] split = userofmsg.split("\\+");

        for (String s : split) {
            log.info("这是你的句子:"+s);
        }

        if(split.length==0)
        {
            //没有上一句
            return usermessage;
        }

        int len=split.length;

        String s="";

        for(int i=len-1;i>=0;i--)
        {
            if(!split[i].contains("d"))
            {
                s = split[i];
                i=-1;
            }
        }

        log.info("这是联系上下文的句子:"+s);

        String[] splitoldmsg = s.split("\\.");

        if(splitoldmsg.length<=1)
        {
            //没有上一句
            return usermessage;
        }

        String Nlpquestionold = splitoldmsg[1];

        log.info("需要拼接的专业名词:"+Nlpquestionold);

        return Nlpquestionold+usermessage;
    }


    /**
     * 缓存用户的输入和回答的输出
     * @param surl
     * @param date
     * @param usermessage
     * @param answer
     */
    private void MsgToRedis(String surl,String date,String username,String usermessage,String answer) {

        //存储患者问题,按照一天为间隔
        redisUtil.hset(getDate()+"Partner:","名字:"+username+"-id号:"+surl+"-时间:"+date,usermessage);

        int onlinneuser=ChatController.getOnlineCount();

        //存储答案,按照一天为间隔
        redisUtil.hset(getDate()+"Doctor:","名字:"+username+"-id号:"+surl+"-时间:"+date,answer);

        log.info("识别号:"+surl+"的信息存入了Redis了");
    }

    /**
     * 将每一次的数据进行一个封装
     * @param usermessage
     * @return
     */
    public String TitleString(String usermessage){
        /**
         * String title=strings.get(0);
         *
         *  String NLPquestion=strings.get(1);
         *
         *   String none=strings.get(2);
         */
        ArrayList<String> strings = sparkUntil.StepArray(usermessage);

        //获取三个元素
        String title=strings.get(0);

        String NLPquestion=strings.get(1);

        String none=strings.get(2);

        return title+"."+NLPquestion+"."+none+"."+"+";
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
        int i,j;

        //取三天的的信息
        for (i=0;i<day;i++)
        {
            //获取每天的用户提问
            Map<Object, Object> o = redisUtil.hmget(getDate(i) + "Partner:");
            //利用,分割每个问题
            String[] split = o.toString().split(",");
            //这是每一个用户的提问
            for (String s : split) {
                //利用=分割出key和value
                String[] split1 = s.split("=");
                //可能存在null字符串所以需要判断
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
        /*for (Map.Entry<String, Integer> stringIntegerEntry : hashMap.entrySet()) {
            log.info("Key = " + stringIntegerEntry.getKey() + ", Value = " + stringIntegerEntry.getValue());
        }*/

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

    //关闭
    @OnClose
    @SneakyThrows
    public void onClose(Session session) {

        //从容器中删除指定的用户
        onlineUsers.remove(session);

        ChatController.subOnlineCount();

    }

    @OnError
    @SneakyThrows
    public void onError(Session session, Throwable error) {

        log.error("错误:"+session.getRequestURI() + " 原因:" + error.getMessage());

        session.getBasicRemote().sendText(getOnlineCount()-1+"个人与你正在一同使用.建议重新刷新页面");

        error.printStackTrace();
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        ChatController.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        ChatController.onlineCount--;
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

}

