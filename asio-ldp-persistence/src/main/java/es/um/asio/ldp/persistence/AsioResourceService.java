package es.um.asio.ldp.persistence;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import org.apache.commons.rdf.api.Dataset;
import org.apache.commons.rdf.api.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trellisldp.api.Metadata;
import org.trellisldp.api.Resource;
import org.trellisldp.api.ResourceService;

public class AsioResourceService implements ResourceService {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AsioResourceService.class);

    @Override
    public CompletionStage<? extends Resource> get(IRI identifier) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompletionStage<Void> replace(Metadata metadata, Dataset dataset) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Metadata metadata) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompletionStage<Void> add(IRI identifier, Dataset dataset) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompletionStage<Void> touch(IRI identifier) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<IRI> supportedInteractionModels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String generateIdentifier() {
        // TODO Auto-generated method stub
        return null;
    }
}
