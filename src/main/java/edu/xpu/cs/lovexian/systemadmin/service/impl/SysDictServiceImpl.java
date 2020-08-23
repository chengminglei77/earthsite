package edu.xpu.cs.lovexian.systemadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.utils.SortUtil;
import edu.xpu.cs.lovexian.systemadmin.domain.SysDict;
import edu.xpu.cs.lovexian.systemadmin.mapper.SysDictMapper;
import edu.xpu.cs.lovexian.systemadmin.service.ISysDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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
@Service("iSysDictService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {
    @Override
    public IPage<SysDict> findDicts(QueryRequest request, SysDict dict) {
        try {
            LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(dict.getDictKey())) {
                queryWrapper.eq(SysDict::getDictKey, dict.getDictKey());
            }
            if (StringUtils.isNotBlank(dict.getDictValue())) {
                queryWrapper.eq(SysDict::getDictValue, dict.getDictValue());
            }
            if (StringUtils.isNotBlank(dict.getTableName())) {
                queryWrapper.eq(SysDict::getTableName, dict.getTableName());
            }
            if (StringUtils.isNotBlank(dict.getFieldName())) {
                queryWrapper.eq(SysDict::getFieldName, dict.getFieldName());
            }

            Page<SysDict> page = new Page<>();
            SortUtil.handlePageSort(request, page, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createDict(SysDict dict) {
        this.save(dict);
    }

    @Override
    @Transactional
    public void updateDict(SysDict dict) {
        this.baseMapper.updateById(dict);
    }

    @Override
    @Transactional
    public void deleteDicts(String[] dictIds) {
        List<String> list = Arrays.asList(dictIds);
        this.baseMapper.deleteBatchIds(list);
    }
}
