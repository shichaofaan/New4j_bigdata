package demo.Entity.Mysql;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * user用户信息
 */
@Data                        //set get
@AllArgsConstructor         //有参
@NoArgsConstructor
@TableName("user")         //映射数据库表名字
@Accessors(chain = true)  //链式写法
@ToString
@ApiModel("用户类")
public class User {

    //默认全局主键唯一
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @TableField(value = "user_name")
    @NotNull(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @TableField(value = "user_emil")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",message = "不是标准邮箱号")
    @NotNull(message = "邮箱不能为空")
    @ApiModelProperty(value = "用户邮箱")
    private String userEmil;

    @TableField(value = "user_number")
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$",message = "不是标准张号")
    @NotNull(message = "帐号不能为空")
    @ApiModelProperty(value = "用户账号")
    private String userNumber;

    @TableField(value = "user_password")
    @NotNull(message = "密码不能为空")
    @Size(min = 6,max = 6)
    @ApiModelProperty(value = "用户密码")
    private String userPassword;

    //mybatis-plus的独有操作:当插入操作时候自动填充时间
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private String createTime;

    //mybatis-plus的独有操作:当插入修改时自动填充时间
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_time")
    private String updateTime;

    //乐观锁
    @TableField(value = "version")
    @Version
    private Integer version;

    //逻辑删除 0代表不删除 1代表删除
    @TableField(value = "isdelete")
    @TableLogic
    private Integer isdelete;

    //权限 0代表普通用户 1代表管理员
    @TableField(value = "power")
    private Integer power;

}
