package demo.Spark;

import demo.service.Neo4j.DiseaseRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.internal.value.ListValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SparkUntil {

    @Autowired
    private SpakrToPlainBayes spakrToPlainBayes;

    @Autowired
    private DiseaseRepository diseaseRepository;


    @SneakyThrows
    public ArrayList<String> StepArray(String question){
        //将状态数组进行返回
        return spakrToPlainBayes.analysisQuery(question);
    }

    /**
     * 问题回答的依据
     * @param question
     * @return
     */
    @SneakyThrows
    public String answer(String question){
        String answer = "";

        boolean flag=false;

        /**
         * 利用朴素贝叶斯打散问题.得到模板化答案
         */
        ArrayList<String> strings = spakrToPlainBayes.analysisQuery(question);

        String title=strings.get(0);

        String NLPquestion=strings.get(1);

        String SameNLPquestion=Modification(NLPquestion);

        SameNLPquestion=SameNLPquestion.concat(".*");

        log.info("词语模糊之后的样子:"+SameNLPquestion);

        String none=strings.get(2);

        //第二次进来的适合如果没有模板直接返回废话
        if(question.contains("这是第二次的msg")&&title.equals("999")){
            answer="我只是弱人工智能.仅仅只能用于医疗问答模型.";
            return answer;
        }

        log.info("贝叶斯模板是:"+title+"---提取出来的名词是:"+NLPquestion+"---词性是:"+none);

        answer=answer.concat(NLPquestion+"的"+none+":");

        log.info("模板是:"+answer);

        //如果疾病在这个库里面不存在
        if(title.equals("999")&&(NLPquestion.equals("dz")||NLPquestion.equals("sym"))){
            answer="我只是弱人工智能.仅仅只能用于医疗问答模型.";
            return answer;
        }


        switch(title) {
            case "0":
                //根据疾病查症状  返回数组
                String[] str = diseaseRepository.SymptomToDisease(NLPquestion);

                if(str.length==0)
                {
                    //这下必有答案
                    str = diseaseRepository.SameSymptomToDisease(SameNLPquestion);
                }

                if(str!=null){

                    flag=true;
                    for (String s : str) {
                        //拼接字符串
                        answer=answer.concat(s);
                        answer=answer.concat(".");
                    }

                }

                break;
            case "1":
                //根据疾病查病因
                answer = diseaseRepository.DiseaseToCause(NLPquestion);

                if(answer==null)
                {
                    //这下必有答案
                    answer = diseaseRepository.SameDiseaseToCause(SameNLPquestion);
                }

                if(answer!=null){
                    flag=true;
                }

                break;
            case "2":
                //根据疾病查预防措施
                answer = diseaseRepository.DiseaseToPrevent(NLPquestion);

                if(answer==null)
                {
                    //这下必有答案
                    answer = diseaseRepository.SameDiseaseToPrevent(SameNLPquestion);
                }

                if(answer!=null){
                    flag=true;
                }

                break;
            case "3":
                //根据疾病查治愈时间
                answer = diseaseRepository.DiseaseToCureLasttime(NLPquestion);

                if(answer==null)
                {
                    //这下必有答案
                    answer = diseaseRepository.SameDiseaseToCureLasttime(SameNLPquestion);
                }

                if(answer!=null){
                    flag=true;
                }

                break;
            case "4":
                //根据疾病查治愈概率
                answer = diseaseRepository.DiseaseToCureProb(NLPquestion);

                if(answer==null)
                {
                    //这下必有答案
                    answer = diseaseRepository.SameDiseaseToCureProb(SameNLPquestion);
                }

                if(answer!=null){
                    flag=true;
                }

                break;
            case "5":
                //根据疾病查治愈方法
                List<ListValue> listValues = diseaseRepository.DiseaseToCureWay(NLPquestion);

                if (listValues==null) {
                    //这下必有答案
                    listValues = diseaseRepository.SameDiseaseToCureWay(SameNLPquestion);
                }

                if(listValues.size()>0){

                    List<Object> objects = listValues.get(0).asList();

                    for (Object string : objects) {
                        flag=true;
                        answer=answer.concat(string.toString());
                        answer=answer.concat(".");
                    }

                }

                break;
            case "6":
                //根据疾病查易感人群
                answer = diseaseRepository.DiseaseToEasyGet(NLPquestion);

                if(answer==null)
                {
                    //这下必有答案
                    answer = diseaseRepository.SameDiseaseToEasyGet(SameNLPquestion);
                }

                if(answer!=null){
                    flag=true;
                }

                break;
            case "7":
                //根据疾病查疾病描述
                answer = diseaseRepository.DiseaseToDesc(NLPquestion);

                if(answer==null)
                {
                    //这下必有答案
                    answer = diseaseRepository.SameDiseaseToDesc(SameNLPquestion);
                }

                if(answer!=null){
                    flag=true;
                }

                break;
            case "8":
                //根据疾病查忌口
                String[] strings2 = diseaseRepository.DiseaseToNoeat(NLPquestion);

                if(strings2!=null){
                    //这下必有答案
                    strings2 = diseaseRepository.SameDiseaseToNoeat(SameNLPquestion);
                }

                if(strings2!=null){

                    flag=true;
                    for (String s : strings2) {
                        //不能吃的食物
                        answer=answer.concat(s);
                        answer=answer.concat(".");
                    }
                }


                break;
            case "9":
                //根据疾病查适合吃的食物
                String[] strings3 = diseaseRepository.DiseaseToDiseaseDoFood(NLPquestion);

                if(strings3!=null){
                    //这下必有答案
                    strings3 = diseaseRepository.SameDiseaseToDiseaseDoFood(SameNLPquestion);
                }

                if(strings3!=null){

                    flag=true;
                    for (String s : strings3) {
                        //不能吃的食物
                        answer=answer.concat(s);
                        answer=answer.concat(".");
                    }
                }

                break;
            case "10":
                //根据疾病查推荐药品
                String[] strings4 = diseaseRepository.DiseaseToCommonDrug(NLPquestion);

                if(strings4!=null){
                    //这下必有答案
                    strings4 = diseaseRepository.SameDiseaseToCommonDrug(SameNLPquestion);
                }

                if(strings4!=null){

                    flag=true;
                    for (String s : strings4) {
                        //吃的药物
                        answer=answer.concat(s);
                        answer=answer.concat(".");
                    }
                }


                break;
            case "11":
                //根据疾病查检查方案
                String[] strings5 = diseaseRepository.DiseaseToCheck(NLPquestion);

                if(strings5!=null){
                    //这下必有答案
                    strings5 = diseaseRepository.SameDiseaseToCheck(SameNLPquestion);
                }

                if(strings5!=null){

                    flag=true;
                    for (String s : strings5) {
                        //检查方案
                        answer=answer.concat(s);
                        answer=answer.concat(".");
                    }
                }

                break;
            case "12":
                //根据疾病查并发症
                answer = diseaseRepository.DiseaseToDisease(NLPquestion);

                if(answer==null)
                {
                    //这下必有答案
                    answer = diseaseRepository.SameDiseaseToDisease(SameNLPquestion);
                }

                if(answer!=null){
                    flag=true;
                }

                break;
            case "13":
                //根据症状查疾病
                String[] strings6 = diseaseRepository.DiseaseToSymptom(NLPquestion);

                if(strings6!=null){
                    //这下必有答案
                    strings6 = diseaseRepository.SameDiseaseToSymptom(SameNLPquestion);
                }

                if(strings6!=null){

                    flag=true;
                    for (String s : strings6) {
                        answer=answer.concat(s);
                        answer=answer.concat(".");
                    }
                }

                break;
            default:

                if(NLPquestion.equals("dz")||NLPquestion.equals("sym")) {
                    answer="请不要问一些无关的问题.我的时间很宝贵的.";
                    flag=true;
                }

                log.info("天天开心快乐.");
        }

        //如果flag为false证明库里面没有答案
        if(!flag)
        {
            if(NLPquestion.equals("dz")||NLPquestion.equals("sym"))
            {
                answer="这个语句我很陌生.我需要学习一下~~~";
            }else {
                //根据疾病查疾病描述
                answer = diseaseRepository.DiseaseToDesc(NLPquestion);

                if(answer==null)
                {
                    //这下必有答案
                    answer = diseaseRepository.SameDiseaseToDesc(SameNLPquestion);
                }
                if(answer==null)
                {
                    answer="我知道你想问'"+NLPquestion+"'有关的但是很抱歉.我不具有所有病状的检查和分析.你可以去预约专家进行问诊治疗";
                }
            }
        }

        return answer;
    }

    /**
     * 模糊查询
     * @param NLPquestion
     * @return
     */
    private static String Modification(String NLPquestion){
        char[] chars = NLPquestion.toCharArray();

        String resultQuestion="";

        for (char aChar : chars) {
            //先拼接
            resultQuestion=resultQuestion.concat(".*");
            //后拼接
            resultQuestion=resultQuestion.concat(String.valueOf(aChar));
        }

        return resultQuestion;

    }
}
