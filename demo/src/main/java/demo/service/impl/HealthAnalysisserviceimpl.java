package demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo.Entity.Mysql.HealthAnalysis;
import demo.Mapper.HealthAnalysisMapper;
import demo.service.HealthAnalysisservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HealthAnalysisserviceimpl extends ServiceImpl<HealthAnalysisMapper, HealthAnalysis> implements HealthAnalysisservice {
}
