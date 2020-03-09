/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.um.asio.ldp.app;

import org.apache.commons.lang3.StringUtils;
import org.trellisldp.dropwizard.AbstractTrellisApplication;
import org.trellisldp.http.core.ServiceBundler;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * A deployable Trellis application.
 */
public class TrellisApplication extends AbstractTrellisApplication<AppConfiguration> {

    /**
     * Service bundler
     */
    private ServiceBundler serviceBundler;

    /**
     * The main entry point.
     *
     * @param args
     *            the argument list
     * @throws Exception
     *             if something goes horribly awry
     */
    public static void main(final String[] args) throws Exception {
        String[] arguments;

        final String envConfigLocation = System.getenv(ENV_CONFIG_LOCATION);

        if (args.length > 0) {
            arguments = args;
        } else if (StringUtils.isNotBlank(envConfigLocation)) {
            arguments = new String[] { "server", envConfigLocation };
        } else {
            arguments = new String[] { "server", "config-dev.yml" };
        }

        new TrellisApplication().run(arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ServiceBundler getServiceBundler() {
        return this.serviceBundler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialize(final AppConfiguration config, final Environment environment) {
        super.initialize(config, environment);
        this.serviceBundler = new TrellisServiceBundler(config, environment);
    }

    @Override
    public void initialize(final Bootstrap<AppConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceFileConfigurationSourceProvider());
        super.initialize(bootstrap);
    }

    /**
     * LDP configuration file
     */
    private static final String ENV_CONFIG_LOCATION = "ASIO_LDP_CONFIG_FILE";
}
