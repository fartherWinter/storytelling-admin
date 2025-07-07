package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.bo.SysRolePartitionBo;
import com.chennian.storytelling.bean.model.SysRole;
import com.chennian.storytelling.bean.model.SysRoleMenu;
import com.chennian.storytelling.bean.model.SysUserRole;
import com.chennian.storytelling.common.constant.UserConstants;
import com.chennian.storytelling.common.exception.ServiceException;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.utils.StringUtils;
import com.chennian.storytelling.dao.SysRoleDeptMapper;
import com.chennian.storytelling.dao.SysRoleMenuMapper;
import com.chennian.storytelling.dao.SysUserRoleMapper;
import com.chennian.storytelling.service.SysRoleService;
import com.chennian.storytelling.dao.SysRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

/**
* @author chen
* @createDate 2025-05-06 19:11:40
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{
    
    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleDeptMapper sysRoleDeptMapper;
    
    public SysRoleServiceImpl(SysRoleMapper sysRoleMapper, SysRoleDeptMapper sysRoleDeptMapper, SysUserRoleMapper sysUserRoleMapper, SysRoleMenuMapper sysRoleMenuMapper ) {
        this.sysRoleMapper = sysRoleMapper;
        this.sysRoleDeptMapper = sysRoleDeptMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysRoleMenuMapper = sysRoleMenuMapper;
    }
    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        return sysRoleMapper.selectRoleList(role);
    }

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        return sysRoleMapper.selectRolesByUserId(userId);
    }

    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> roles = selectRolesByUserId(userId);
        return roles.stream()
                .filter(role -> "0".equals(role.getStatus()))
                .map(SysRole::getRoleKey)
                .collect(Collectors.toSet());
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return selectRoleList(new SysRole());
    }

    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
            new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        );
        return userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        return sysRoleMapper.selectRoleById(roleId);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = sysRoleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = sysRoleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new StorytellingBindException("不允许操作超级管理员角色");
        }
    }

    @Override
    public void checkRoleDataScope(Long roleId) {
        if (roleId != null && roleId == 1L) {
            throw new StorytellingBindException("不允许修改超级管理员角色");
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return sysUserRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(SysRole role) {
        // 新增角色信息
        sysRoleMapper.insertRole(role);
        //新增角色菜单关联
        return insertRoleMenu(role);
    }


    /**
     * 新增角色菜单信息
     */
    private int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            if (menuId == 554) {
                System.out.println("Insert");
            }
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = sysRoleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRole role) {
        sysRoleMapper.updateRole(role);
        sysRoleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    @Override
    public int updateRoleStatus(SysRole role) {
        return sysRoleMapper.updateRole(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int authDataScope(SysRole role) {
        checkRoleAllowed(role);
        return sysRoleMapper.updateRole(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleById(Long roleId) {
        SysRole role = selectRoleById(roleId);
        checkRoleAllowed(role);
        
        // 检查角色是否被使用
        if (countUserRoleByRoleId(roleId) > 0) {
            throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
        }
        
        // 删除角色与菜单关联
        sysRoleMenuMapper.deleteRoleMenuByRoleId(roleId);
        // 删除角色与部门关联
        sysRoleDeptMapper.deleteRoleDeptByRoleId(roleId);
        
        return sysRoleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        //删除角色与菜单关联
        sysRoleMenuMapper.deleteRoleMenu(roleIds);
        //删除角色与部门关联
        sysRoleDeptMapper.deleteRoleDept(roleIds);
        return sysRoleMapper.deleteRoleByIds(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAuthUser(SysUserRole userRole) {
        return sysUserRoleMapper.delete(
            new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userRole.getUserId())
                .eq(SysUserRole::getRoleId, userRole.getRoleId())
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return sysUserRoleMapper.delete(
            new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, roleId)
                .in(SysUserRole::getUserId, userIds)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAuthUsers(Long roleId, Long[] userIds) {
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : userIds) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return sysUserRoleMapper.batchUserRole(list);
    }
    
    // ==================== 工作流角色相关方法实现 ====================
    
    /**
     * 查询工作流角色列表
     * @return 工作流角色列表
     */
    @Override
    public List<SysRole> selectWorkflowRoles() {
        return sysRoleMapper.selectList(
            new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleType, "WORKFLOW")
                .eq(SysRole::getStatus, "0")
                .orderByAsc(SysRole::getRoleSort)
        );
    }
    
    /**
     * 根据用户ID和部门ID查询角色列表
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRolesByUserIdAndDept(Long userId, Long deptId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<SysUserRole>()
            .eq(SysUserRole::getUserId, userId)
            .eq(SysUserRole::getEnabled, 1);
            
        if (deptId != null) {
            wrapper.eq(SysUserRole::getDepartmentId, deptId);
        }
        
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(wrapper);
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Long> roleIds = userRoles.stream()
            .map(SysUserRole::getRoleId)
            .distinct()
            .toList();
            
        return sysRoleMapper.selectBatchIds(roleIds);
    }
    
    /**
     * 为用户分配角色（支持部门、生效时间等）
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param deptId 部门ID
     * @return 分配结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoleToUser(Long userId, Long roleId, Long deptId) {
        if (userId == null || roleId == null) {
            return false;
        }
        
        // 检查角色是否存在
        SysRole role = selectRoleById(roleId);
        if (role == null) {
            throw new ServiceException("角色不存在");
        }
        
        // 检查是否已经分配过该角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<SysUserRole>()
            .eq(SysUserRole::getUserId, userId)
            .eq(SysUserRole::getRoleId, roleId);
            
        if (deptId != null) {
            wrapper.eq(SysUserRole::getDepartmentId, deptId);
        }
        
        SysUserRole existingUserRole = sysUserRoleMapper.selectOne(wrapper);
        if (existingUserRole != null) {
            // 如果已存在但被禁用，则启用它
            if (existingUserRole.getEnabled() != 1) {
                existingUserRole.setEnabled(1);
                return sysUserRoleMapper.updateById(existingUserRole) > 0;
            }
            return true; // 已经分配过了
        }
        
        // 创建新的用户角色关联
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setDepartmentId(deptId);
        userRole.setEnabled(1);
        userRole.setEffectiveDate(LocalDateTime.now());
        
        return sysUserRoleMapper.insert(userRole) > 0;
    }
    
    /**
     * 根据角色类型查询角色列表
     * @param roleType 角色类型（SYSTEM、WORKFLOW、CUSTOM）
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRolesByType(String roleType) {
        if (StringUtils.isEmpty(roleType)) {
            return selectRoleAll();
        }
        
        return sysRoleMapper.selectList(
            new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleType, roleType)
                .eq(SysRole::getStatus, "0")
                .orderByAsc(SysRole::getRoleSort)
        );
    }
    
    /**
     * 检查用户在指定部门是否具有指定角色
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param deptId 部门ID
     * @return 是否具有角色
     */
    @Override
    public boolean hasRoleInDept(Long userId, Long roleId, Long deptId) {
        if (userId == null || roleId == null) {
            return false;
        }
        
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<SysUserRole>()
            .eq(SysUserRole::getUserId, userId)
            .eq(SysUserRole::getRoleId, roleId)
            .eq(SysUserRole::getEnabled, 1);
            
        if (deptId != null) {
            wrapper.eq(SysUserRole::getDepartmentId, deptId);
        }
        
        return sysUserRoleMapper.selectCount(wrapper) > 0;
    }
}




