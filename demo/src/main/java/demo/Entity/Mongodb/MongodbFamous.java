package demo.Entity.Mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "FamousMsg")        //对应Mongodb里面的实体类
@Accessors(chain = true)
public class MongodbFamous {

    @Id
    private Integer id;

    //答案
    @Field("answer")
    private String FamousMsg;
}
