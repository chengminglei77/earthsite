package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DtusAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDtusAdminService;
import edu.xpu.cs.lovexian.app.appadmin.utils.StatusEnum;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service实现
 *
 * @author xpu
 * @date 2020-09-01 21:29:32
 */
@Service("dtusAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DtusAdminServiceImpl extends ServiceImpl<DtusAdminMapper, AdminDtus> implements IDtusAdminService {

    @Autowired
    private DtusAdminMapper dtusAdminMapper;

    @Override
    public IPage<AdminDtus> findDtuss(QueryRequest request, AdminDtus adminDtus) {
        QueryWrapper<AdminDtus> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        Page<AdminDtus> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    /**
     * 删除和查找操作放在一个实现方法上
     *
     * @param request
     * @param adminDtus
     * @return
     */
    @Override
    public IPage<AdminDtus> findDtusByTypeId(QueryRequest request, AdminDtus adminDtus) {
        QueryWrapper<AdminDtus> queryWrapper = new QueryWrapper<>();

        //如果DtuName不为空,那么就模糊查询:dtu名
        //如果两者都符合,那么就SELECT COUNT(1) FROM dtus WHERE dtu_name LIKE '%%' AND dtu_type LIKE '%%' AND status = 0;
        if (StringUtils.isNotBlank(adminDtus.getId())) {
            queryWrapper.lambda().like(AdminDtus::getId, adminDtus.getId()).orderByDesc(AdminDtus::getCreatedAt);
        }
        if (StringUtils.isNotBlank(adminDtus.getDtuName())) {
            queryWrapper.lambda().like(AdminDtus::getDtuName, adminDtus.getDtuName()).orderByDesc(AdminDtus::getCreatedAt);
        }

        if (adminDtus.getStatus() != null) {
            //相当于where status=....
            queryWrapper.lambda().eq(AdminDtus::getStatus, adminDtus.getStatus()).orderByDesc(AdminDtus::getCreatedAt);
        }/*else{
            adminDtus.setStatus(StatusEnum.NORMAL_STATE.getCode());//0为未删除状态
            System.out.println("查询为数据的标志state=="+adminDtus.getStatus());
            queryWrapper.lambda().eq(AdminDtus::getStatus,adminDtus.getStatus());
        }*/

        if (adminDtus.getDelState() != null) {
            queryWrapper.lambda().eq(AdminDtus::getDelState, adminDtus.getDelState()).orderByDesc(AdminDtus::getCreatedAt);
        } else {
            adminDtus.setDelState(StatusEnum.NORMAL_STATE.getCode());//0为未删除状态
            System.out.println("查询为数据的标志delstate==" + adminDtus.getDelState());
            queryWrapper.lambda().eq(AdminDtus::getDelState, adminDtus.getDelState()).orderByDesc(AdminDtus::getCreatedAt);
        }
        Page<AdminDtus> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);

    }


    @Override
    public boolean deleteDtus(String id) {
        UpdateWrapper<AdminDtus> updateWrapper = new UpdateWrapper<>();
        //删除操作实际上做的是将status设置为1,从而不是真正意义上的在数据库删除,只是不在前端界面显示而已
        updateWrapper.lambda().eq(AdminDtus::getId, id).set(AdminDtus::getDelState, 1);
        return this.update(updateWrapper);
    }

    @Override
    public boolean restoreDtus(String id) {
        UpdateWrapper<AdminDtus> updateWrapper = new UpdateWrapper<>();
        //还原逻辑删除的报警信息
        updateWrapper.lambda().eq(AdminDtus::getId, id).set(AdminDtus::getDelState, 0);
        return this.update(updateWrapper);
    }


    @Override
    public IPage<AdminDtus> queryDtuInfo(QueryRequest request, AdminDtus adminDtus) {
        QueryWrapper<AdminDtus> queryWrapper = new QueryWrapper<>();
        //查询对应字段dtuInfo
        if (adminDtus.getDtuName() != null) {
            queryWrapper.lambda().eq(AdminDtus::getDtuName, adminDtus.getDtuName());
        }
        Page<AdminDtus> adminDtusPage = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(adminDtusPage, queryWrapper);
    }


    @Override
    public boolean completelyDeleteDtus(String id) {
        dtusAdminMapper.deleteById(id);
        return true;
    }

    @Override
    public boolean getDtuId(String dtuId) {
        dtusAdminMapper.selectById(dtuId);
        return true;
    }

  /*  @Override
    public boolean autoUpdateDtus(AdminDtus adminDtus) {
        adminDtus.setUpdatedAt(new Date());
        adminDtus.setStatus(0);
        QueryWrapper<AdminDtus> queryWrapper = new QueryWrapper<>();
        if (adminDtus.getUpdatedAt() != null) {
            queryWrapper.lambda().eq(AdminDtus::getUpdatedAt, adminDtus.getUpdatedAt());
        }
        IService<AdminDtus> dtusAdminService;
        dtusAdminService.getOne(queryWrapper);
        boolean actOper = dtusAdminService.saveOrUpdate(adminDtus);

        return true;
    }*/
}
