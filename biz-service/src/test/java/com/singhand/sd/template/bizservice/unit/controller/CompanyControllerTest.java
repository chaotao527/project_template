package com.singhand.sd.template.bizservice.unit.controller;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @version v1.0
 * @description:
 * @author: shifeng
 * @create 2023/8/10 16:27
 */
@SpringBootTest
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class CompanyControllerTest {

    protected Faker faker = new Faker(new Locale("zh-CN"));
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void queryByPage() {
        //mockMvc.perform(post().accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(document("query"));
    }

    @Test
    @SneakyThrows
    void queryById() {
        final var name = faker.name().name();
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").type("bigint").description("id"),
                fieldWithPath("name").description("name"),
                subsectionWithPath("users").description("公司员工信息"),
                /*fieldWithPath("users[].id").description("员工id"),
                fieldWithPath("users[].name").description("员工姓名"),*/
/*                subsectionWithPath("users[].company").description("员工所属公司"),
                fieldWithPath("users[].company.id").description("员工所属公司id"),
                fieldWithPath("users[].company.name").description("员工所属公司名称"),*/
                fieldWithPath("users[].roles[]").description("人物所属角色"),
                fieldWithPath("not_exist").type(JsonFieldType.STRING).optional().description("不存在的字段"),
    };
        final var mvcResult = mockMvc.perform(get("/company/{id}" ,1))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}", relaxedResponseFields(book), RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("id").description("公司id")),
                        responseFields(beneathPath("users[].company"),
                                fieldWithPath("id").description("The forecast high in degrees celcius"),
                                fieldWithPath("name").description("The forecast low in degrees celcius"))))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        log.info(content);
    }

    @Test
    @SneakyThrows
    void queryById1() {
        final var name = faker.name().name();
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").type("bigint").description("id"),
                fieldWithPath("name").description("name"),
                subsectionWithPath("users").description("公司员工信息"),
                fieldWithPath("users[].roles[]").description("人物所属角色"),
                fieldWithPath("not_exist").type(JsonFieldType.STRING).optional().description("不存在的字段"),
        };
        var sni = relaxedResponseFields(book);
        final var mvcResult = mockMvc.perform(get("/company/{id}" ,1))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",sni.and(fieldWithPath("not_exist1").type(JsonFieldType.STRING).optional().description("不存在的字段1")) , RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("id").description("公司id")),
                        responseFields(beneathPath("users[].company"),
                                fieldWithPath("id").description("The forecast high in degrees celcius"),
                                fieldWithPath("name").description("The forecast low in degrees celcius"))))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        log.info(content);
    }
}