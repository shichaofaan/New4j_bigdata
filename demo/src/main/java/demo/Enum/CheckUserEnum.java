package demo.Enum;

import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * code: 数据校验码
 * msg: 数据异常信息
 * */
@NoArgsConstructor
public enum CheckUserEnum implements Serializable{

    Right(0,"正确"){
        @Override
        public boolean isflag() {	//覆盖枚举的flag方法
            return true;
        }
    },
    NumberError(1,"账号输入错误"),
    KeyError(2,"密码错误"),
    NumberException(3,"账号异常"),
    NumberDisabled(4,"账号被禁用"),
    EmailError(5,"邮箱验证码错误"),
    SystemException(6,"系统异常"),
    EmailTextError(7,"邮箱格式错误"),
    NumberExist(8,"账号已经存在无法重新注册(名字或者邮箱重复,或者都重复)");

    private int index;	        //存放状态码
    private String msg;       //存放状态信息

    private CheckUserEnum(int index) {	   //构造方法必须是私有的
        this.index = index;
        this.msg="";
    }

    CheckUserEnum(int index, String msg) {    //这个默认是私有的
        this.index = index;
        this.msg = msg;
    }

    public int getIndex() {
        return index;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "状态码: "+index+" 状态信息: "+msg;
    }

    //除了Right其他全为false
    public boolean isflag() {
        return false;
    }
}