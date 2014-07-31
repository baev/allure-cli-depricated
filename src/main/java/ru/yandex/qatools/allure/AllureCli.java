package ru.yandex.qatools.allure;

import io.airlift.command.Arguments;
import io.airlift.command.Command;
import io.airlift.command.HelpOption;
import io.airlift.command.Option;
import io.airlift.command.OptionType;
import io.airlift.command.ParseException;
import io.airlift.command.SingleCommand;
import org.codehaus.plexus.util.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.report.AllureReportBuilder;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.05.14
 */
@Command(name = "allure", description = "Allure report generation utility")
public class AllureCli {

    private static final String DEFAULT_OUTPUT_PATH = "output";

    private static final String DEFAULT_REPORT_VERSION = "1.3.9";

    private static final Logger logger = LoggerFactory.getLogger(AllureCli.class);

    @Inject
    public HelpOption helpOption;

    @Option(type = OptionType.COMMAND,
            name = {"-o", "--outputPath"},
            description = "Directory to output report files to (default is \"" + DEFAULT_OUTPUT_PATH + "\")")
    public String outputPath = DEFAULT_OUTPUT_PATH;

    @Arguments(title = "input directories or globs",
            description = "A list of input directories or globs to be processed")
    public List<String> inputPathsOrGlobs = new ArrayList<>();

    @Option(name = {"--version"}, description = "Show CLI version")
    public boolean version;

    @Option(name = {"-v", "--reportVersion"}, description = "Report version specification in Maven format (supports ranges)")
    public String reportVersion = DEFAULT_REPORT_VERSION;

    @SuppressWarnings("unused")
    @Option(name = {"--verbose"}, description = "Turn on verbose mode")
    public boolean verbose;

    public static void main(String[] args) {
        AllureCli allure;
        try {
            allure = SingleCommand.singleCommand(AllureCli.class).parse(args);
        } catch (ParseException e) {
            error(e.getMessage());
            System.exit(1);
            return;
        }

        if (allure.helpOption.showHelpIfRequested()) {
            return;
        }

        if (!allure.version && allure.inputPathsOrGlobs.isEmpty()) {
            error("No directories for input XML files specified.");
            return;
        }

        allure.run();
    }

    public void run() {
        if (version) {
            version();
            return;
        }

        generate();
    }

    /**
     * Generate allure report to outputPath using data from inputPathsOrGlobs
     */
    public void generate() {
        try {
            File outputDirectory = new File(outputPath);
            if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
                throw new RuntimeException("Can't create output directory " + outputPath);
            }

            debug("Generating report for Allure version = " + reportVersion + "...");
            List<File> inputDirectories = getInputDirectories(inputPathsOrGlobs);
            AllureReportBuilder allureReportBuilder = new AllureReportBuilder(reportVersion, outputDirectory);
            allureReportBuilder.processResults(inputDirectories.toArray(new File[inputDirectories.size()]));

//            //FIXME: Uncomment this when https://github.com/allure-framework/allure-report-builder/issues/1 is resolved
//            if (generator.getTestRunGenerator().getListFiles().getFiles().isEmpty()){
//                error("No valid Allure XML files found in specified directory.");
//                return;
//            }

            allureReportBuilder.unpackFace();

            info(String.format(
                    "Successfully generated report to %s.",
                    outputDirectory.getAbsolutePath()
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Show CLI version
     */
    public void version() {
        try {
            final URL url = ((URLClassLoader) getClass().getClassLoader()).findResource("META-INF/MANIFEST.MF");
            final Manifest manifest = new Manifest(url.openStream());
            final String specificationVersion = manifest.getMainAttributes().getValue("Specification-Version");
            if (specificationVersion != null) {
                info(specificationVersion);
            } else {
                info("Failed to load version from MANIFEST.MF. This is probably a bug.");
            }
        } catch (IOException e) {
            throw new RuntimeException("An exception while loading CLI version.", e);
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
            debug("Processing input path or glob = " + inputPathOrGlob);
            if (inputPathOrGlob.indexOf('~') == 0) {
                inputPathOrGlob = inputPathOrGlob.replace("~", System.getProperty("user.home"));
            }
            File inputDirectory = new File(inputPathOrGlob);
            if (inputDirectory.exists() && inputDirectory.canRead()) {
                //Directory was specified
                debug("Adding " + inputPathOrGlob + " as input directory.");
                inputDirectories.add(inputDirectory);
            } else {
                //Trying Ant-style globs
                String[] globs = inputPathOrGlob.split(";");
                File[] foundInputDirectories = getPathsByGlobs(getCurrentWorkingDirectory(), globs);
                for (File foundInputDirectory : foundInputDirectories) {
                    debug("Processing " + foundInputDirectory + " inferred from globs.");
                    if (foundInputDirectory.exists() && foundInputDirectory.canRead()) {
                        debug("Adding " + foundInputDirectory + " as input directory.");
                        inputDirectories.add(foundInputDirectory);
                    }
                }
            }
        }
        return inputDirectories;
    }

    private static void error(String message) {
        logger.error(message);
    }

    private void info(String message) {
        logger.info(message);
    }

    private void debug(String message) {
        if (verbose) {
            logger.info(message);
        }
    }
}
