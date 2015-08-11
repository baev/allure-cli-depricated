import java.nio.file.Paths

import static ru.yandex.qatools.allure.TestHelper.checkReportDirectory

def outputDirectory = Paths.get(basedir.absolutePath, 'allure-report')
checkReportDirectory(outputDirectory, 1)