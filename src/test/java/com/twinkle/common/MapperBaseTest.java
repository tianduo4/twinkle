package com.twinkle.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
/**
 * description: mapper单元测试辅助类
 *
 * @author ：King
 * @date ：2019/1/13 14:56
 */
@SpringContext({"classpath*:spring-test-context-mapper.xml"})
public class MapperBaseTest extends Test4J {
    protected static final Logger logger = LoggerFactory.getLogger(MapperBaseTest.class);
}
