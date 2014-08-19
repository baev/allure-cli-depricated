package ru.yandex.qatools.allure.command;

import io.airlift.command.Arguments;
import io.airlift.command.Command;
import io.airlift.command.Option;
import org.codehaus.plexus.util.DirectoryScanner;
import ru.yandex.qatools.allure.report.AllureReportBuilder;
import ru.yandex.qatools.allure.report.AllureReportBuilderException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * eroshenkoam
 * 11/08/14
 */
@Command(name = "generate", description = "Generate Allure report")
public class ReportGenerate extends ReportCommand {

    @Arguments(title = "Results patterns", required = true,
            description = "A list of input directories or globs to be processed")
    public List<String> resultsPatterns;

    @Option(name = {"-v", "--report-version"}, required = false,
            description = "Report version specification in Maven format")
    public String reportVersion = getConfig().getReportVersion();

    public void runUnsafe() throws AllureReportBuilderException {
        File outputDirectory = new File(reportPath);
        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
            throw new RuntimeException("Can't create output directory " + reportPath);
        }
        getLogger().debug(String.format("Generating report for Allure version [%s]", reportVersion));
        List<File> inputDirectories = getResultsDirectories(resultsPatterns);
        for (File file : inputDirectories) {
            getLogger().debug(String.format("Found results directory [%s]", file.getAbsolutePath()));
        }
        AllureReportBuilder allureReportBuilder = new AllureReportBuilder(reportVersion, outputDirectory);
        allureReportBuilder.processResults(inputDirectories.toArray(new File[inputDirectories.size()]));

        allureReportBuilder.unpackFace();
        getLogger().info(String.format("Successfully generated report to [%s].", outputDirectory.getAbsolutePath()));
    }

    protected List<File> getResultsDirectories(List<String> resultPatterns) {
        List<File> resultDirectories = new ArrayList<>();
        for (String resultPattern : resultPatterns) {
            getLogger().debug(String.format("Processing result pattern [%s]", resultPattern));
            resultDirectories.addAll(getResultsDirectoryByPattern(resultPattern));
        }
        return resultDirectories;
    }

    public List<File> getResultsDirectoryByPattern(String resultsPattern) {
        File absoluteDirectory = new File(resultsPattern);
        if (absoluteDirectory.isAbsolute() && absoluteDirectory.isDirectory() &&
                absoluteDirectory.exists() && absoluteDirectory.canRead()) {
            return Arrays.asList(absoluteDirectory);
        } else {
            return Arrays.asList(getPathsByGlobs(getCurrentWorkingDirectory(), resultsPattern));
        }
    }

    private File[] getPathsByGlobs(File baseDir, String globs) {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(baseDir);
        scanner.setIncludes(new String[]{globs});
        scanner.setCaseSensitive(false);
        scanner.scan();

        String[] relativePaths = scanner.getIncludedDirectories();
        File[] absolutePaths = new File[relativePaths.length];
        for (int i = 0; i < relativePaths.length; i++) {
            absolutePaths[i] = new File(baseDir, relativePaths[i]);
        }
        return absolutePaths;
    }

    private File getCurrentWorkingDirectory() {
        return new File(System.getProperty("user.dir"));
    }

}
