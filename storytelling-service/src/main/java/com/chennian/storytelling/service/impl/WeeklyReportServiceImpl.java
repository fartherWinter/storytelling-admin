package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.bean.dto.WeeklyReportDTO;
import com.chennian.storytelling.dao.WeeklyReportMapper;
import com.chennian.storytelling.service.WeeklyReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 周报服务实现类
 * 
 * @author TraeAI
 */
@Service
public class WeeklyReportServiceImpl implements WeeklyReportService {

    private static final Logger log = LoggerFactory.getLogger(WeeklyReportServiceImpl.class);

    @Autowired
    private WeeklyReportMapper weeklyReportMapper;

    @Override
    @Transactional
    public Long createWeeklyReport(WeeklyReportDTO weeklyReportDTO) {
        // weeklyReportDTO.setCreateTime(java.time.LocalDateTime.now()); // 由数据库自动生成
        // weeklyReportDTO.setUpdateTime(java.time.LocalDateTime.now()); // 由数据库自动生成
        weeklyReportMapper.insertWeeklyReport(weeklyReportDTO); // MyBatis会回填ID到weeklyReportDTO对象
        log.info("创建周报成功: {}", weeklyReportDTO);
        return weeklyReportDTO.getId();
    }

    @Override
    @Transactional
    public boolean updateWeeklyReport(WeeklyReportDTO weeklyReportDTO) {
        if (weeklyReportDTO.getId() == null) {
            log.warn("更新周报失败，周报ID不能为空");
            return false;
        }
        // weeklyReportDTO.setUpdateTime(java.time.LocalDateTime.now()); // 由数据库自动生成或更新触发器处理
        int affectedRows = weeklyReportMapper.updateWeeklyReport(weeklyReportDTO);
        if (affectedRows > 0) {
            log.info("更新周报成功: {}", weeklyReportDTO);
            return true;
        } else {
            log.warn("更新周报失败，未找到或未更新周报: {}", weeklyReportDTO.getId());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteWeeklyReport(Long id) {
        if (id == null) {
            log.warn("删除周报失败，周报ID不能为空");
            return false;
        }
        int affectedRows = weeklyReportMapper.deleteWeeklyReportById(id);
        if (affectedRows > 0) {
            log.info("删除周报成功: {}", id);
            return true;
        } else {
            log.warn("删除周报失败，未找到周报: {}", id);
            return false;
        }
    }

    @Override
    public WeeklyReportDTO getWeeklyReportById(Long id) {
        if (id == null) {
            log.warn("查询周报失败，周报ID不能为空");
            return null;
        }
        WeeklyReportDTO report = weeklyReportMapper.selectWeeklyReportById(id);
        if (report == null) {
            log.info("未找到ID为 {} 的周报", id);
        }
        return report;
    }

    @Override
    public List<WeeklyReportDTO> listWeeklyReportsByUserId(Long userId) {
        if (userId == null) {
            log.warn("查询用户周报列表失败，用户ID不能为空");
            return List.of(); // 返回空列表而不是null，更安全
        }
        List<WeeklyReportDTO> userReports = weeklyReportMapper.listWeeklyReportsByUserId(userId);
        log.info("查询用户 {} 的周报列表，共 {} 条", userId, userReports.size());
        return userReports;
    }

    @Override
    public WeeklyReportDTO getWeeklyReportByUserIdAndDateRange(Long userId, LocalDate weekStartDate) {
        if (userId == null || weekStartDate == null) {
            log.warn("查询用户在指定周的周报失败，用户ID或周开始日期不能为空");
            return null;
        }
        WeeklyReportDTO report = weeklyReportMapper.selectWeeklyReportByUserIdAndDateRange(userId, weekStartDate);
        if (report == null) {
            log.info("未查询到用户 {} 在 {} 开始的周的周报", userId, weekStartDate);
        }
        return report;
    }
}