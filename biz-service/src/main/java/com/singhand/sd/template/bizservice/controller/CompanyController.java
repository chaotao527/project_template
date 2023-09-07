package com.singhand.sd.template.bizservice.controller;

import com.singhand.sd.template.bizcommon.model.CompanyVo;
import com.singhand.sd.template.bizcommon.service.CompanyService;
import com.singhand.sd.template.bizmodel.model.Company;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @describe: 描述
 * @author: chaotao
 * @create_date: 2023/3/9 11:44
 * @version: V1.0
 **/
@Tag(name = "机构管理")
@RestController
@RequestMapping("company")
@Slf4j
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @PostMapping
    @Operation(summary = "新建机构")
    @SneakyThrows
    public Company newCompany(@Valid @RequestBody Company company) {
        return companyService.newCompany(company);
    }

    @PutMapping
    @Operation(summary = "更新机构")
    @SneakyThrows
    public Company updateCompany(@Valid @RequestBody CompanyVo companyVo) {
        return companyService.updateCompany(companyVo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询机构详情")
    @SneakyThrows
    public CompanyVo getById(@Valid @PathVariable Long id) {
        return companyService.getById(id);
    }
}
