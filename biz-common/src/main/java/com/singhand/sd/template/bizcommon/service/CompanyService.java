package com.singhand.sd.template.bizcommon.service;

import com.singhand.sd.template.bizcommon.model.CompanyVo;
import com.singhand.sd.template.bizmodel.model.Company;

public interface CompanyService{

    Company newCompany(Company company);

    Company updateCompany(CompanyVo companyVo);

    CompanyVo getById(Long id);
}
