package util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogConfigurator {
    public static void configureLogs(){
        boolean logFileEnable = PropertiesLoader.isLogFileEnable();
        String logFileName = PropertiesLoader.getLogFileName();
        String logLevel = PropertiesLoader.getLogLevel();

        LoggerContext contextLogger = (LoggerContext) LogManager.getContext(false);
        Configuration config = contextLogger.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig("logger");
        loggerConfig.setLevel(Level.getLevel(logLevel));
        final Layout<String> layout = PatternLayout.createLayout("%d [%-5level] %m %n",null, config,
                null, null, false, false, null, null);
        if (logFileEnable) {
            if (PropertiesLoader.isLogFilePreserve()) {
                logFileName = logFileName + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            }
            FileAppender fileAppender = FileAppender.createAppender(PropertiesLoader.getLogDir() + "/" + logFileName + ".log", "false", "false",
                    "File", "false", "false", "false", "4000", layout, null, "false", null, config);
            fileAppender.start();
            loggerConfig.addAppender(fileAppender, Level.getLevel(logLevel), null);
        }
    }
}
