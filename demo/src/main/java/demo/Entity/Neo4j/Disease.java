package demo.Entity.Neo4j;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.neo4j.driver.internal.value.ListValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.io.Serializable;
import java.util.List;

@Data
@Node(value = "Disease")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)  //链式写法
public class Disease implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    //得病原因
    @Property(name = "cause")
    private String cause;

    //疾病所属的部门  数组
    @Property("cure_department")
    private List<String> cure_department;

    //疗程
    @Property("cure_lasttime")
    private String cure_lasttime;

    //治疗方法    数组
    @Property("cure_way")
    private List<ListValue> cure_way;

    //治疗概率
    @Property("cured_prob")
    private String cured_prob;

    //疾病描述
    @Property("desc")
    private String desc;

    //疾病易得人群
    @Property("easy_get")
    private String easy_get;

    //疾病名字
    @Property("name")
    private String name;

    //疾病预防
    @Property("prevent")
    private String prevent;
}
