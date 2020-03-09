package es.um.asio.ldp.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.configuration.ConfigurationSourceProvider;

/**
 * An implementation of {@link ConfigurationSourceProvider} which reads the configuration
 * from a resource file.
 * <p>
 * In order to abide by the calling conventions of
 * {ClassLoader#getResourceAsStream} [1], absolute path strings
 * (i.e. those with leading "/" characters) passed to {@link #open(String)}
 * are converted to relative paths by removing the leading "/".
 * <p>
 * See [1] for more information on resources in Java and how they are
 * loaded at runtime.
 * <p>
 * [1] https://docs.oracle.com/javase/8/docs/technotes/guides/lang/resources.html
 */
public class ResourceFileConfigurationSourceProvider implements ConfigurationSourceProvider {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceFileConfigurationSourceProvider.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream open(final String path) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("PATH -> {}", path);
        }

        InputStream result = this.getResourceAsStream(path);
        result = (result == null) && path.startsWith("/") ? this.getResourceAsStream(path.substring(1)) : result;

        if (result == null) {
            result = this.getFile(path);
        }

        return result;
    }

    private InputStream getResourceAsStream(final String path) {
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }

    private InputStream getFile(final String path) throws FileNotFoundException {
        final File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + file + " not found");
        }

        return new FileInputStream(file);
    }
}
