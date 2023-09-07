package com.singhand.sd.template.testenvironments.listener;

import com.singhand.sd.template.testenvironments.helper.DataHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
public class MyTestExecutionListener implements TestExecutionListener, Ordered {

    @Autowired
    private DataHelper dataHelper;


    @Autowired
    @Qualifier("bizTransactionManager")
    private PlatformTransactionManager bizTransactionManager;

    @Override
    public void beforeTestClass(TestContext testContext) {

        testContext.getApplicationContext()
                .getAutowireCapableBeanFactory()
                .autowireBean(this);
    }

    @Override
    public void beforeTestMethod(@NonNull TestContext testContext) throws Exception {

        TestExecutionListener.super.beforeTestMethod(testContext);

        final var annotations = testContext.getTestMethod().getAnnotations();
        for (final var annotation : annotations) {
      /*if (annotation instanceof MockVertex mockVertex) {
        newVertex(mockVertex);
      } else if (annotation instanceof MockVertices mockVertices) {
        newVertices(mockVertices);
      } else if (annotation instanceof MockDataSource mockDataSource) {
        getOrCreateDataSource(mockDataSource);
      } else if (annotation instanceof MockDataSources mockDataSources) {
        newDataSources(mockDataSources);
      }*/
        }
    }


    @Override
    public int getOrder() {

        return Integer.MAX_VALUE;
    }
}
