package by.itechart.util;

import by.itechart.BaseTest;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestResultLoggerExtension implements TestWatcher, AfterAllCallback {
    private List<TestResultStatus> testResultsStatus = new ArrayList<>();
    Logger log = BaseTest.getLog();

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        log.info("Test disabled for test {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.DISABLED);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        log.info("Test successful for test {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.SUCCESSFUL);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        log.info("Test Aborted for test {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.ABORTED);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        try (InputStream is = Files.newInputStream(Paths.get("trace.zip"))) {
            Allure.attachment("trace.zip", is);
        } catch (IOException e) {
            log.error("File for input stream was not found");
        }

        try (InputStream is = Files.newInputStream(Paths.get("final_screenshot.png"))) {
            Allure.attachment("failure_screenshot", is);
        } catch (IOException e) {
            log.error("File for input stream was not found");
        }

        try (InputStream is = Files.newInputStream(Paths.get(PropertiesLoader.getLogDir() + "/"
                + LogConfigurator.getLogFileName() + ".log"))) {
            Allure.attachment("log file", is);
        } catch (IOException e) {
            log.error("File for input stream was not found");
        }
        log.info("Test failed for test {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.FAILED);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        Map<TestResultStatus, Long> summary = testResultsStatus.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        log.info("Test result summary for {} {}", context.getDisplayName(), summary.toString());
    }

    private enum TestResultStatus {
        SUCCESSFUL, ABORTED, FAILED, DISABLED;
    }
}

