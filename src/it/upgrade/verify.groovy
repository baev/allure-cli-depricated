import java.nio.file.Files
import java.nio.file.Paths

def bundles = Paths.get(basedir.absolutePath, '.allure', 'bundles')
assert Files.exists(bundles)
assert Files.newDirectoryStream(bundles).iterator().hasNext()