package ru.yandex.qatools.allure.command;

import io.airlift.command.Arguments;
import io.airlift.command.Command;
import io.airlift.command.Option;
import org.codehaus.plexus.util.DirectoryScanner;
import ru.yandex.qatools.allure.report.AllureReportBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * eroshenkoam
 * 11/08/14
 */
@Command(name = "generate", description = "Generate allure report")
public class ReportGenerate extends AllureCommand {

    private static final String DEFAULT_OUTPUT_PATH = "output";

    private static final String DEFAULT_REPORT_VERSION = "1.3.9";

    @Arguments(title = "input directories or globs", required = true,
            description = "A list of input directories or globs to be processed")
    public List<String> resultsPatterns;

    @Option(name = {"-o", "--report-path"}, required = false,
            description = "Directory to output report files to (default is \"" + DEFAULT_OUTPUT_PATH + "\")")
    public String reportPath = DEFAULT_OUTPUT_PATH;

    @Option(name = {"-v", "--report-version"}, required = false,
            description = "Report version specification in Maven format (supports ranges)")
    public String reportVersion = DEFAULT_REPORT_VERSION;

    public void run() {
        try {
            File outputDirectory = new File(reportPath);
            if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
                throw new RuntimeException("Can't create output directory " + reportPath);
            }

            List<File> inputDirectories = getInputDirectories(resultsPatterns);
            AllureReportBuilder allureReportBuilder = new AllureReportBuilder(reportVersion, outputDirectory);
            allureReportBuilder.processResults(inputDirectories.toArray(new File[inputDirectories.size()]));

            allureReportBuilder.unpackFace();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File getCurrentWorkingDirectory() {
        return new File(System.getProperty("user.dir"));
    }

    private File[] getPathsByGlobs(File baseDir, String[] globs) {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(baseDir);
        scanner.setIncludes(globs);
        scanner.setCaseSensitive(false);
        scanner.scan();

        String[] relativePaths = scanner.getIncludedDirectories();
        File[] absolutePaths = new File[relativePaths.length];
        for (int i = 0; i < relativePaths.length; i++) {
            absolutePaths[i] = (new File(baseDir, relativePaths[i]));
        }
        return absolutePaths;
    }

    protected List<File> getInputDirectories(List<String> inputPathsOrGlobs) {
        List<File> inputDirectories = new ArrayList<>();
        for (String inputPathOrGlob : inputPathsOrGlobs) {
            if (inputPathOrGlob.indexOf('~') == 0) {
                inputPathOrGlob = inputPathOrGlob.replace("~", System.getProperty("user.home"));
            }
            File inputDirectory = new File(inputPathOrGlob);
            if (inputDirectory.exists() && inputDirectory.canRead()) {
                //Directory was specified
                inputDirectories.add(inputDirectory);
            } else {
                //Trying Ant-style globs
                String[] globs = inputPathOrGlob.split(";");
                File[] foundInputDirectories = getPathsByGlobs(getCurrentWorkingDirectory(), globs);
                for (File foundInputDirectory : foundInputDirectories) {
                    if (foundInputDirectory.exists() && foundInputDirectory.canRead()) {
                        inputDirectories.add(foundInputDirectory);
                    }
                }
            }
        }
        return inputDirectories;
    }
}
