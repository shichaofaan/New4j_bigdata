package demo.service.Neo4j;


import demo.Entity.Neo4j.Disease;
import org.neo4j.driver.internal.value.ListValue;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 疾病的查找所有属性
 */
@Repository
public interface DiseaseRepository extends Neo4jRepository<Disease, Long> {

    /**
     * 查找节点Disease的n个节点
     * @param num_limit
     * @return
     */
    @Query(value = "match (n:Disease) return n limit $num_limit")
    List<Disease> findByName(@Param(value = "num_limit") long num_limit);

    /**
     * 查找疾病的症状
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:has_symptom]->(n:Symptom) where m.name = $DiseaseName return n.name")
    String[] SymptomToDisease(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的症状       模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:has_symptom]->(n:Symptom) where m.name =~ $DiseaseName return n.name limit 5")
    String[] SameSymptomToDisease(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的病因
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) WHERE m.name = $DiseaseName return m.cause")
    String DiseaseToCause(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的病因           模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) WHERE m.name =~ $DiseaseName return m.cause limit 1")
    String SameDiseaseToCause(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病预防措施
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name = $DiseaseName return m.prevent")
    String DiseaseToPrevent(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病预防措施         模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name =~ $DiseaseName return m.prevent limit 1")
    String SameDiseaseToPrevent(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的治愈时间
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name = $DiseaseName return  m.cure_lasttime")
    String DiseaseToCureLasttime(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的治愈时间           模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name =~ $DiseaseName return  m.cure_lasttime limit 1")
    String SameDiseaseToCureLasttime(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的治愈概率
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name = $DiseaseName return  m.cured_prob")
    String DiseaseToCureProb(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的治愈概率            模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name =~ $DiseaseName return  m.cured_prob limit 1")
    String SameDiseaseToCureProb(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的治愈方法
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name = $DiseaseName return  m.cure_way")
    List<ListValue> DiseaseToCureWay(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的治愈方法       模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name =~ $DiseaseName return  m.cure_way limit 1")
    List<ListValue> SameDiseaseToCureWay(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的易感人群
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name = $DiseaseName return  m.easy_get")
    String DiseaseToEasyGet(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的易感人群   模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name =~ $DiseaseName return  m.easy_get limit 1")
    String SameDiseaseToEasyGet(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的描述
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name = $DiseaseName return  m.desc")
    String DiseaseToDesc(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的描述   模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease) where m.name =~ $DiseaseName return  m.desc limit 1")
    String SameDiseaseToDesc(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的忌口
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:no_eat]->(n:Food) where m.name = $DiseaseName return n.name")
    String[] DiseaseToNoeat(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的忌口      模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:no_eat]->(n:Food) where m.name =~ $DiseaseName return n.name limit 5")
    String[] SameDiseaseToNoeat(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的适合吃的食物
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:do_eat]->(n:Food) where m.name = $DiseaseName return n.name")
    String[] DiseaseToDiseaseDoFood(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的适合吃的食物  模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:do_eat]->(n:Food) where m.name =~ $DiseaseName return n.name limit 5")
    String[] SameDiseaseToDiseaseDoFood(@Param(value = "DiseaseName") String DiseaseName);

//    /**
//     * 查找疾病的推荐食谱
//     * @param DiseaseName
//     * @return
//     */
//    @Query(value = "MATCH (m:Disease)-[r:recommand_eat]->(n:Food) where m.name = $DiseaseName return n.name")
//    String[] DiseaseToRecommandEat(@Param(value = "DiseaseName") String DiseaseName);

//    /**
//     * 好评药品
//     * 根据疾病推荐好评药品
//     * @param DiseaseName
//     * @return
//     */
//    @Query(value = "MATCH (m:Disease)-[r:recommand_drug]->(n:Drug) where m.name = $DiseaseName return n.name")
//    String[] DiseaseToRecommandDrug(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 根据疾病推荐药品
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:common_drug]->(n:Drug) where m.name = $DiseaseName return n.name")
    String[] DiseaseToCommonDrug(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 根据疾病推荐药品   模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:common_drug]->(n:Drug) where m.name =~ $DiseaseName return n.name limit 5")
    String[] SameDiseaseToCommonDrug(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查询疾病应该进行的检查
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:need_check]->(n:Check) where m.name = $DiseaseName return n.name")
    String[] DiseaseToCheck(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查询疾病应该进行的检查  模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:need_check]->(n:Check) where m.name =~ $DiseaseName return n.name limit 3")
    String[] SameDiseaseToCheck(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的并发症
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:acompany_with]->(n:Disease) where m.name = $DiseaseName return n.name")
    String DiseaseToDisease(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找疾病的并发症     模糊
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:acompany_with]->(n:Disease) where m.name =~ $DiseaseName return n.name limit 1")
    String SameDiseaseToDisease(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * 查找症状会得哪些疾病
     * @param DiseaseSymptom
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:has_symptom]->(n:Symptom) where n.name = $DiseaseSymptom return m.name")
    String[] DiseaseToSymptom(@Param(value = "DiseaseSymptom") String DiseaseSymptom);

    /**
     * 查找症状会得哪些疾病        模糊
     * @param DiseaseSymptom
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:has_symptom]->(n:Symptom) where n.name =~ $DiseaseSymptom return m.name limit 5")
    String[] SameDiseaseToSymptom(@Param(value = "DiseaseSymptom") String DiseaseSymptom);






    /**
     * 查找哪些疾病会导致这个疾病
     * @param DiseaseName
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:acompany_with]->(n:Disease) where n.name = $DiseaseName return m.name")
    String[] DiseaseFromDisease(@Param(value = "DiseaseName") String DiseaseName);

    /**
     * no_eat
     * 根据忌口食品查找疾病
     * @param Foodname
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:no_eat]->(n:Food) where n.name = $Foodname return m.name")
    String[] NoFoodToDisease(@Param(value = "Foodname") String Foodname);

    /**
     * do_eat
     * 根据可以吃的食品查找疾病
     * @param Foodname
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:do_eat]->(n:Food) where n.name = $Foodname return m.name")
    String[] FoodToDisease(@Param(value = "Foodname") String Foodname);

    /**
     * 好评药品
     * 根据食谱推荐疾病
     * @param RecommandEat
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:recommand_eat]->(n:Food) where n.name = $RecommandEat return m.name")
    String[] RecommandEatToDisease(@Param(value = "RecommandEat") String RecommandEat);

    /**
     * 已知药品查询能够治疗的疾病
     * @param CommonDrug
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:common_drug]->(n:Drug) where n.name = $CommonDrug return m.name")
    String[] CommonDrugToDisease(@Param(value = "CommonDrug") String CommonDrug);

    /**
     * 已知推荐药品查询能够治疗的疾病
     * @param RecommandDrug
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:recommand_drug]->(n:Drug) where n.name = $RecommandDrug return m.name")
    String[] RecommandDrugToDisease(@Param(value = "RecommandDrug") String RecommandDrug);

    /**
     * 已知检查查询疾病
     * @param Check
     * @return
     */
    @Query(value = "MATCH (m:Disease)-[r:need_check]->(n:Check) where n.name = $Check return m.name")
    String[] CheckToDisease(@Param(value = "Check") String Check);
}
