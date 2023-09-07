package com.singhand.sd.template.bizcommon.model;

import cn.hutool.core.bean.BeanUtil;
import com.singhand.sd.template.bizmodel.model.Company;
import com.singhand.sd.template.bizmodel.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @describe: 描述
 * @author: chaotao
 * @create_date: 2023/6/20 15:12
 * @version: V1.0
 **/
@Data
@NoArgsConstructor
public class CompanyVo {

    private Long ID;

    private String name;

    private Set<User> users = new HashSet<>();

    public CompanyVo(Company company) {
        BeanUtil.copyProperties(company, this, "modules");
    }
}
