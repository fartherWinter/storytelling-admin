package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.bean.dto.DailyReportDTO;

import com.chennian.storytelling.dao.DailyReportMapper;
import com.chennian.storytelling.service.DailyReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 日报服务实现类
 * 
 * @author TraeAI
 */
@Service
public class DailyReportServiceImpl implements DailyReportService {

    private static final Logger log = LoggerFactory.getLogger(DailyReportServiceImpl.class);

    @Autowired
    private DailyReportMapper dailyReportMapper;

    @Override
    @Transactional
    public Long createDailyReport(DailyReportDTO dailyReportDTO) {
        // dailyReportDTO.setCreateTime(java.time.LocalDateTime.now()); // 由数据库自动生成
        // dailyReportDTO.setUpdateTime(java.time.LocalDateTime.now()); // 由数据库自动生成
        dailyReportMapper.insertDailyReport(dailyReportDTO); // MyBatis会回填ID到dailyReportDTO对象
        log.info("创建日报成功: {}", dailyReportDTO);
        return dailyReportDTO.getId();
    }

    @Override
    @Transactional
    public boolean updateDailyReport(DailyReportDTO dailyReportDTO) {
        if (dailyReportDTO.getId() == null) {
            log.warn("更新日报失败，日报ID不能为空");
            return false;
        }
        // dailyReportDTO.setUpdateTime(java.time.LocalDateTime.now()); // 由数据库自动生成或更新触发器处理
        int affectedRows = dailyReportMapper.updateDailyReport(dailyReportDTO);
        if (affectedRows > 0) {
            log.info("更新日报成功: {}", dailyReportDTO);
            return true;
        } else {
            log.warn("更新日报失败，未找到或未更新日报: {}", dailyReportDTO.getId());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteDailyReport(Long id) {
        if (id == null) {
            log.warn("删除日报失败，日报ID不能为空");
            return false;
        }
        int affectedRows = dailyReportMapper.deleteDailyReportById(id);
        if (affectedRows > 0) {
            log.info("删除日报成功: {}", id);
            return true;
        } else {
            log.warn("删除日报失败，未找到日报: {}", id);
            return false;
        }
    }

    @Override
    public DailyReportDTO getDailyReportById(Long id) {
        if (id == null) {
            log.warn("查询日报失败，日报ID不能为空");
            return null;
        }
        DailyReportDTO report = dailyReportMapper.selectDailyReportById(id);
        if (report == null) {
            log.info("未找到ID为 {} 的日报", id);
        }
        return report;
    }

    @Override
    public List<DailyReportDTO> listDailyReportsByUserId(Long userId) {
        if (userId == null) {
            log.warn("查询用户日报列表失败，用户ID不能为空");
            return List.of(); // 返回空列表而不是null，更安全
        }
        List<DailyReportDTO> userReports = dailyReportMapper.listDailyReportsByUserId(userId);
        log.info("查询用户 {} 的日报列表，共 {} 条", userId, userReports.size());
        return userReports;
    }

    @Override
    public DailyReportDTO getDailyReportByUserIdAndDate(Long userId, LocalDate reportDate) {
        if (userId == null || reportDate == null) {
            log.warn("查询用户在指定日期的日报失败，用户ID或报告日期不能为空");
            return null;
        }
        DailyReportDTO report = dailyReportMapper.selectDailyReportByUserIdAndDate(userId, reportDate);
        if (report == null) {
            log.info("未查询到用户 {} 在 {} 的日报", userId, reportDate);
        }
        return report;
    }
}