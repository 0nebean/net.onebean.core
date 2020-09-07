package net.onebean.core.velocity;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Velocity 引擎工厂类
 * @author World
 */
public class VelocityEngineFactory {

    private static final Logger logger = LoggerFactory.getLogger(VelocityEngineFactory.class);


    private static VelocityEngine engine = null;
    static {
        engine = new VelocityEngine();
        // 可选值："class"--从classpath中读取，"file"--从文件系统中读取
        engine.setProperty("resource.loader", "class");
        // 如果从文件系统中读取模板，那么属性值为org.apache.velocity.runtime.resource.loader.FileResourceLoader
        engine.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            engine.init();
        } catch (Exception e) {
            logger.error("VelocityEngine init error = ",e);
        }
    }


    private static class LazyHolder {
        private static final VelocityEngineFactory INSTANCE = new VelocityEngineFactory();
    }

    private VelocityEngineFactory(){}

    public static VelocityEngineFactory getInstance() {
        return LazyHolder.INSTANCE;
    }

    public VelocityEngine getEngine() {
        return engine;
    }

}
