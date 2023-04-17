package demo.Controller;

import cn.dev33.satoken.stp.StpUtil;
import demo.Entity.Mysql.HealthAnalysis;
import demo.Entity.Mysql.User;
import demo.common.R;
import demo.service.impl.HealthAnalysisserviceimpl;
import demo.service.impl.Userserviceimpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/Finduserinformation")
@Slf4j
@Api(tags = "UserMsgController")
@Component
public class UserMsgController {

    @Resource
    private Userserviceimpl userserviceimpl;

    @Resource
    private HealthAnalysisserviceimpl healthAnalysisserviceimpl;

    /**
     * 查询用户信息进行包装返回
     * @return
     */
    @ApiOperation(value = "主页面")
    @PostMapping(value = "/user")
    public User welcomehome(){
        User user;
        //根据账号去数据库里面取信息
        String loginId =(String) StpUtil.getLoginId();
        user = userserviceimpl.SelectUserbyID(loginId);
        return user;
    }

    /**
     * 查询用户信息进行包装返回
     * @return
     */
    @ApiOperation(value = "主页面")
    @PostMapping(value = "/senduserMsg")
    public R senduserMsg(@RequestParam("height") String height, @RequestParam("weight") String weight,
                         @RequestParam("hearrate") String hearrate, @RequestParam("bloodpressure") String bloodpressure,
                         @RequestParam("bloodsugar") String bloodsugar, @RequestParam("bloodlipids") String bloodlipids,
                         @RequestParam("name") String name){

        HealthAnalysis healthAnalysis=new HealthAnalysis();

        //封装成用户类
        healthAnalysis.setBloodlipids(bloodlipids)
                .setBloodpressure(bloodpressure)
                .setBloodsugar(bloodsugar)
                .setHearrate(hearrate)
                .setHeight(height)
                .setWeight(weight)
                .setName(name);

        //存入mysql
        boolean save = healthAnalysisserviceimpl.save(healthAnalysis);

        if(save){
            log.info(name+"用户健康信息存入成功");
        }else {
            log.info(name+"用户健康信息存入失败");
        }

        String Msg=ResultMsg(healthAnalysis);

        return R.ok("200",Msg);
    }

    /**
     * 得到答案
     * @param healthAnalysis
     * @return
     */
    private String ResultMsg(HealthAnalysis healthAnalysis){

        String ReslultMsg="";

        Integer height = Integer.valueOf(healthAnalysis.getHeight());
        //转换成m
        height=height/100;

        Integer weight = Integer.valueOf(healthAnalysis.getWeight());

        //获取血糖
        Double bloodsugar =  Double.valueOf(healthAnalysis.getBloodsugar());
        //获取血脂
        Double Bloodlipids=Double.valueOf(healthAnalysis.getBloodlipids());
        //获取血压
        Double Bloodpressure=Double.valueOf(healthAnalysis.getBloodpressure());
        //获取心率
        Double Hearrate=Double.valueOf(healthAnalysis.getHearrate());

        //计算BMI
        ReslultMsg= ReslultMsg.concat(BMI(weight * 1.0, height * 1.0));
        //计算血糖
        ReslultMsg= ReslultMsg.concat(bloodsugar(bloodsugar));
        //计算血脂
        ReslultMsg= ReslultMsg.concat(bloodlipids(Bloodlipids));
        //计算血压
        ReslultMsg= ReslultMsg.concat(bloodpressure(Bloodpressure));
        //计算心率
        ReslultMsg= ReslultMsg.concat(hearrate(Hearrate));

        return ReslultMsg;
    }

    /**
     * 计算血脂
     * @param bloodlipids
     * @return
     */
    private String bloodlipids(Double bloodlipids){
        String msg="";

        if(bloodlipids>=6.2){
            msg = msg.concat("血脂高是体内一种异常的代谢状态，也叫血脂异常，是代谢性疾病，主要由于体内代谢机能的异常，导致正常的饮食摄入或体内物质过度存积，转化脂质存积在血液中，当检测出胆固醇、低密度脂蛋白胆固醇以及甘油三酯等脂质成分高于正常人群的水平，就是高脂血症 血脂异常与人体的代谢有密切关系，可能是遗传因素或基因决定的，也可能是后天的生活方式以及过度的饮食所导致.");
        }else if(bloodlipids<=5.2){
            msg = msg.concat("可能与长期吃素食，饮食量过少，患有肝胆疾病等原因有关。如果长时间饮食不规律，长期处于饥饿的状态，或者是过度的减肥，都有可能使血脂处于偏低的状态 此外，患有肝胆疾病导致胆汁分泌不足，或者是长时间营养不良，甘油三酯的值也会低于正常水平，少数患者还会出现明显的心慌、浑身无力、烦躁不安等症状.");
        }else{
            msg = msg.concat("血脂正常.");
        }

        return msg;
    }

    /**
     * 计算血压
     * @param bloodpressure
     * @return
     */
    private String bloodpressure(Double bloodpressure){
        String msg="";

        if(bloodpressure<=60){
            msg=msg.concat("低血压是指体循环动脉压力低于正常的状态。成年人如果收缩压低于90或者舒张压低于60则为低血压，低血压的治疗重在日常预防，生活要有规律，防止过度疲劳，因为极度疲劳会使血压降得更低.");
        }else if(bloodpressure>=90){
            msg=msg.concat("高血压之所以受到关注是因为它常常引起心、脑、肾等重要脏器的损害。而低血压是和气血虚弱相关联，日常饮食应该多吃营养丰富的食物。吃鸡蛋，牛奶，但也不宜过多，药物方面可以给与生脉饮口服液口服。要保持良好的精神状态，适当加强锻炼，提高身体素质，改善神经、血管的调节功能，加速血液循环，减少直立性低血压的发作。适量饮茶，因茶中的咖啡因能兴奋呼吸中枢及心血管系统.");
        }else {
            msg=msg.concat("血压正常.继续保持.");
        }

        return msg;
    }

    /**
     * 计算心率
     * @param hearrate
     * @return
     */
    private String hearrate(Double hearrate){
        String msg="";
        //如果是高血糖
        if(hearrate>=100){
            msg=msg.concat("心率有点高了.注意是否不能呼吸.注意预防下面疾病.'心包间皮瘤'.'变异型心绞痛.'妊娠性心肌梗死'.'心脏转移瘤.'先天性冠状动脉瘘'.建议多休息.尽量少运动.");
        }else if(hearrate<=60){
            msg=msg.concat("心率有点低了.注意看心脏没有不舒服.注意预防下面疾病.'心绞痛'.'心律失常'.'心力衰竭'.加强运动.");
        }else{
            msg=msg.concat("心率正常.尝试做些运动活动活动.");
        }
        return msg;
    }


    /**
     * 血糖计算
     * @param bloodsugar
     * @return
     */
    private String bloodsugar(Double bloodsugar){
        String msg="";
        //如果是高血糖
        if(bloodsugar>=7.0){
            msg=msg.concat("血糖略高可能得病:'II型糖尿病'.'糖尿病'.'足小儿遗传性果糖不耐受'.'肥胖症'.'肾实质性高血压'.建议多锻炼.");
        }else if(bloodsugar<=3.9){
            msg=msg.concat("血糖略低可能得病:'新生儿低血糖症'.'胰岛功能性β慢性疾病'.'遗传性果糖不耐受'.建议吃点水果和蛋白质.");
        }else{
            msg=msg.concat("血糖保持不错.继续加油.");
        }

        return msg;
    }

    /**
     * BMI计算
     * @param weight
     * @param height
     * @return
     */
    private String BMI(Double weight,Double height){
        String msg="";
        Double BMI= (weight)/(height*height);
        if(BMI>=18.5&&BMI<=23.9){
            msg=msg.concat("身高体重正常.继续保持.");
        }else if(BMI<18.5){
            msg=msg.concat("体重略微有点轻了.多加强营养补充.");
        }else{
            msg=msg.concat("体重略微胖了.多加强运动锻炼.");
        }
        return msg;
    }
}
