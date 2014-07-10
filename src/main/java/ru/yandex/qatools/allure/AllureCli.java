package ru.yandex.qatools.allure;

import io.airlift.command.Arguments;
import io.airlift.command.Command;
import io.airlift.command.HelpOption;
import io.airlift.command.Option;
import io.airlift.command.OptionType;
import io.airlift.command.ParseException;
import io.airlift.command.SingleCommand;
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
    
    private static final String DEFAULT_REPORT_VERSION = "1.4.0.RC4";

    @Inject
    public HelpOption helpOption;

    @Option(type = OptionType.COMMAND,
            name = {"-o", "--outputPath"},
            description = "Directory to output report files to (default is \"" + DEFAULT_OUTPUT_PATH + "\")")
    public String outputPath = DEFAULT_OUTPUT_PATH;

    @Arguments(title = "input directories",
            description = "A list of input directories to be processed")
    public List<String> inputPaths = new ArrayList<>();

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
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        if (allure.helpOption.showHelpIfRequested()) {
            return;
        }
        
        if (!allure.version && allure.inputPaths.isEmpty()){
            System.out.println("No directories for input XML files specified.");
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
     * Generate allure report to outputPath using data from inputPaths
     */
    public void generate() {
        try {
            File outputDirectory = new File(outputPath);
            if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
                throw new RuntimeException("Can't create output directory " + outputPath);
            }

            List<File> inputDirectories = new ArrayList<>();
            for (String inputPath : inputPaths) {
                File inputDirectory = new File(inputPath);
                if (inputDirectory.exists() && inputDirectory.canRead()) {
                    inputDirectories.add(inputDirectory);
                }
            }

            AllureReportBuilder allureReportBuilder = new AllureReportBuilder(reportVersion, outputDirectory);
            allureReportBuilder.processResults(inputDirectories.toArray(new File[inputDirectories.size()]));

//            //FIXME: Uncomment this when https://github.com/allure-framework/allure-report-builder/issues/1 is resolved
//            if (generator.getTestRunGenerator().getListFiles().getFiles().isEmpty()){
//                System.out.println("No valid Allure XML files found in specified directory.");
//                return;
//            }

            allureReportBuilder.unpackFace();
            
            System.out.println(String.format(
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
                System.out.println(specificationVersion);
            } else {
                System.out.println("Failed to load version from MANIFEST.MF. This is probably a bug.");
            }
        } catch (IOException e) {
            throw new RuntimeException("An exception while loading CLI version.", e);
        }
    }
}
