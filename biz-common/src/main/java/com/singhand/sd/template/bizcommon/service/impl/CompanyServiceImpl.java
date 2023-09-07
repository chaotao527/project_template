package com.singhand.sd.template.bizcommon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.singhand.sd.template.bizcommon.model.CompanyVo;
import com.singhand.sd.template.bizmodel.model.Company;
import com.singhand.sd.template.bizmodel.repository.CompanyRepository;
import com.singhand.sd.template.bizcommon.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * @describe: 描述
 * @author: chaotao
 * @create: 2023/9/5 17:49
 **/
@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {

        this.companyRepository = companyRepository;
    }
    @Override
    public Company newCompany(Company company){
        companyRepository.save(company);
        return company;
    }

    @Override
    public Company updateCompany(CompanyVo companyVo) {
        var  managedCompany =companyRepository.findById(companyVo.getID()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "机构不存在！"));
        BeanUtil.copyProperties(companyVo, managedCompany, "id","users");
        managedCompany.getUsers().forEach(item->{
       /* if(!companyVo.getUsers().contains(item)){
          item.setCompany(null);
        }*/
        });
        managedCompany.getUsers().addAll(companyVo.getUsers());
        managedCompany.getUsers().retainAll(companyVo.getUsers());
        companyRepository.save(managedCompany);
        return managedCompany;
    }

    @Override
    public CompanyVo getById(Long id) {
        final var company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "实体不存在！"));
        return new CompanyVo(company);
    }
}
