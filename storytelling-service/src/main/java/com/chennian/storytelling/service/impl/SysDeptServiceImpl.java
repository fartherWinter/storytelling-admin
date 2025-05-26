package com.chennian.storytelling.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.TreeSelect;
import com.chennian.storytelling.bean.model.SysDept;
import com.chennian.storytelling.bean.model.SysUser;
import com.chennian.storytelling.common.constant.UserConstants;
import com.chennian.storytelling.common.exception.ServiceException;
import com.chennian.storytelling.common.utils.StringUtils;
import com.chennian.storytelling.dao.SysDeptMapper;
import com.chennian.storytelling.dao.SysRoleMapper;
import com.chennian.storytelling.dao.SysUserMapper;
import com.chennian.storytelling.service.SysDeptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author chen
* @createDate 2025-05-06 19:12:23
*/
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept>
    implements SysDeptService {

    private final SysDeptMapper sysDeptMapper;
    private final SysUserMapper userMapper;
    
    public SysDeptServiceImpl(SysDeptMapper sysDeptMapper, SysUserMapper userMapper) {
        this.sysDeptMapper = sysDeptMapper;
        this.userMapper = userMapper;
    }

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        List<SysDept> deptList = sysDeptMapper.selectDeptList(dept);
        return buildDeptTree(deptList);
    }

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDept dept) {
        List<SysDept> depts = sysDeptMapper.selectDeptList(dept);
        return buildDeptTreeSelect(depts);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<Long> tempList = depts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        for (SysDept dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 构建前端所需要的下拉树结构
     *
     * @param depts 部门列表
     * @return
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }


    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        return null;
    }

    @Override
    public SysDept selectDeptById(Long deptId) {
        return null;
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return sysDeptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = sysDeptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = sysDeptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = sysDeptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public void checkDeptDataScope(Long deptId) {

    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept) {
        SysDept info = sysDeptMapper.selectDeptById(dept.getParentId());
        if (StringUtils.isNull(info)) {
            return sysDeptMapper.insertDept(dept);
        }
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return sysDeptMapper.insertDept(dept);
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = sysDeptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = sysDeptMapper.selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = sysDeptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        sysDeptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    private void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = sysDeptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            sysDeptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return
     */
    @Override
    public int deleteDeptById(Long deptId) {
        return sysDeptMapper.deleteDeptById(deptId);
    }

    /**
     * 快速修改部门状态
     */
    @Override
    public int changeStatus(SysDept dept) {
        return sysDeptMapper.quickEnable(dept.getDeptId(), dept.getStatus());
    }




    /**
     * 获取父级部门id树
     */
    @Override
    public List<Long> getDepIds(Integer userId) {
        SysUser user = userMapper.selectUserById(Long.valueOf(userId));
        List<Long> deptIds = sysDeptMapper.selectList(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, user.getDeptId()))
                .stream().map(SysDept::getDeptId).collect(Collectors.toList());
        List<Long> depIds = new ArrayList<>(deptIds);
        depIds = getDeptIds(deptIds,depIds);
        depIds.add(user.getDeptId());
        return depIds;
    }

    public List<Long> getDeptIds(List<Long> deptIds, List<Long> depIds) {
        for (Long deptId : deptIds) {
            List<Long> deptIdList = sysDeptMapper.selectList(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, deptId))
                    .stream().map(SysDept::getDeptId).collect(Collectors.toList());
            if (deptIdList.size() > 0) {
                depIds.addAll(deptIdList);
                getDeptIds(deptIdList, depIds);
            }
        }
        return depIds;
    }
}




