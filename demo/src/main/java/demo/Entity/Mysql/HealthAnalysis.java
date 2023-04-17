package demo.Entity.Mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data                        //set get
@AllArgsConstructor         //有参
@NoArgsConstructor
@TableName("HealthAnalysis")         //映射数据库表名字
@Accessors(chain = true)  //链式写法
@ToString
public class HealthAnalysis {

    //默认全局主键唯一
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "用户ID")
    private Long Id;

    @TableField(value = "height")
    private String height;

    @TableField(value = "weight")
    private String weight;

    @TableField(value = "hearrate")
    private String hearrate;

    @TableField(value = "bloodpressure")
    private String bloodpressure;

    @TableField(value = "bloodsugar")
    private String bloodsugar;

    @TableField(value = "bloodlipids")
    private String bloodlipids;

    @TableField(value = "name")
    private String name;

}
