package edu.xpu.cs.lovexian.systemadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.common.domain.EarthSiteConstant;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.domain.Tree;
import edu.xpu.cs.lovexian.common.domain.router.DeptTree;
import edu.xpu.cs.lovexian.common.utils.SortUtil;
import edu.xpu.cs.lovexian.common.utils.TreeUtil;
import edu.xpu.cs.lovexian.systemadmin.domain.SysDept;
import edu.xpu.cs.lovexian.systemadmin.mapper.SysDeptMapper;
import edu.xpu.cs.lovexian.systemadmin.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**                                                                                ____________________
      _                _                                                           < 神兽护体，永无bug! >
    | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
   | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
  | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
                                   |___/|_|                |___/                                ||----w |
                                                                                                ||     ||
 * @author huchengpeng
 */
@Slf4j
@Service("iSysDeptService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Override
    public Map<String, Object> findDepts(QueryRequest request, SysDept dept) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<SysDept> depts = findDepts(dept, request);
            List<Tree<SysDept>> trees = new ArrayList<>();
            buildTrees(trees, depts);
            Tree<SysDept> deptTree = TreeUtil.build(trees);

            result.put("rows", deptTree);
            result.put("total", depts.size());
        } catch (Exception e) {
            log.error("获取部门列表失败", e);
            result.put("rows", null);
            result.put("total", 0);
        }
        return result;
    }

    @Override
    public List<SysDept> findDepts(SysDept dept, QueryRequest request) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getDeptName()))
            queryWrapper.lambda().eq(SysDept::getDeptName, dept.getDeptName());
        if (StringUtils.isNotBlank(dept.getCreateTimeFrom()) && StringUtils.isNotBlank(dept.getCreateTimeTo()))
            queryWrapper.lambda()
                    .ge(SysDept::getCreateTime, dept.getCreateTimeFrom())
                    .le(SysDept::getCreateTime, dept.getCreateTimeTo());
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", EarthSiteConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createDept(SysDept dept) {
        Long parentId = dept.getParentId();
        if (parentId == null)
            dept.setParentId(0L);
        dept.setCreateTime(new Date());
        this.save(dept);
    }

    @Override
    @Transactional
    public void updateDept(SysDept dept) {
        dept.setUpdateTime(new Date());
        this.baseMapper.updateById(dept);
    }

    @Override
    @Transactional
    public void deleteDepts(String[] deptIds) {
        this.delete(Arrays.asList(deptIds));
    }

    private void buildTrees(List<Tree<SysDept>> trees, List<SysDept> depts) {
        depts.forEach(dept -> {
            Tree<SysDept> tree = new Tree<>();
            tree.setId(dept.getDeptId().toString());
            tree.setKey(tree.getId());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getDeptName());
            tree.setCreateTime(dept.getCreateTime());
            tree.setModifyTime(dept.getUpdateTime());
            tree.setOrder(dept.getOrderNum());
            tree.setTitle(tree.getText());
            tree.setValue(tree.getId());
            trees.add(tree);
        });
    }

    private void delete(List<String> deptIds) {
        removeByIds(deptIds);

        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysDept::getParentId, deptIds);
        List<SysDept> depts = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(depts)) {
            List<String> deptIdList = new ArrayList<>();
            depts.forEach(d -> deptIdList.add(String.valueOf(d.getDeptId())));
            this.delete(deptIdList);
        }
    }

    @Override
    public List<DeptTree<SysDept>> findDepts() {
        List<SysDept> depts = this.baseMapper.selectList(new QueryWrapper<>());
        List<DeptTree<SysDept>> trees = this.convertDepts(depts);
        return TreeUtil.buildDeptTree(trees);
    }

    @Override
    public List<DeptTree<SysDept>> findDepts(SysDept dept) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getDeptName())){
            queryWrapper.lambda().eq(SysDept::getDeptName, dept.getDeptName());
        }
        queryWrapper.lambda().orderByAsc(SysDept::getOrderNum);

        List<SysDept> depts = this.baseMapper.selectList(queryWrapper);
        List<DeptTree<SysDept>> trees =  this.convertDepts(depts);
        return TreeUtil.buildDeptTree(trees);
    }

    private List<DeptTree<SysDept>> convertDepts(List<SysDept> depts){
        List<DeptTree<SysDept>> trees = new ArrayList<>();
        depts.forEach(dept -> {
            DeptTree<SysDept> tree = new DeptTree<>();
            tree.setId(String.valueOf(dept.getDeptId()));
            tree.setParentId(String.valueOf(dept.getParentId()));
            tree.setName(dept.getDeptName());
            tree.setData(dept);
            trees.add(tree);
        });
        return trees;
    }
}
