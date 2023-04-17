package demo.Entity.Msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)  //链式写法
/**
 * 客户端发送数据到服务端
 */
public class UserMessage {

    //名字
    private String name;

    //消息
    private String message;

    //@JSONField(format="yyyy-MM-dd HH:mm:ss") 发送时间
    private String sendTime;
}
