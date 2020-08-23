package edu.xpu.cs.lovexian.systemadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.domain.router.DeptTree;
import edu.xpu.cs.lovexian.systemadmin.domain.SysDept;

import java.util.List;
import java.util.Map;

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
public interface ISysDeptService extends IService<SysDept> {
    Map<String, Object> findDepts(QueryRequest request, SysDept dept);

    List<SysDept> findDepts(SysDept dept, QueryRequest request);

    /**
     * 获取部门树（下拉选使用）
     *
     * @return 部门树集合
     */
    List<DeptTree<SysDept>> findDepts();

    /**
     * 获取部门树（树形选择框使用）
     *
     * @return 部门树集合
     */
    List<DeptTree<SysDept>> findDepts(SysDept dept);

    void createDept(SysDept dept);

    void updateDept(SysDept dept);

    void deleteDepts(String[] deptIds);
}
