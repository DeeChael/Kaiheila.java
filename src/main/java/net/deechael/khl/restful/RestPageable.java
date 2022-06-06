package net.deechael.khl.restful;

import net.deechael.khl.RabbitImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RestPageable implements Iterator<RestRoute.CompiledRoute> {

    protected static final Logger Log = LoggerFactory.getLogger(RestPageable.class);

    private final RestRoute.CompiledRoute compiledRoute;
    private final Metadata metadata;
    private int page;

    private RestPageable(RestRoute.CompiledRoute compiledRoute, Metadata metadata) {
        this.compiledRoute = compiledRoute;
        this.metadata = metadata;
        this.page = metadata != null ? this.metadata.getPage() : -1;
    }

    public static RestPageable of(RabbitImpl rabbit, RestRoute.CompiledRoute compiledRoute, JsonNode data) {
        Metadata meta = null;
        if (data.has("meta")) {
            try {
                meta = rabbit.getJsonEngine().readValue(data.get("meta").traverse(), Metadata.class);
            } catch (IOException e) {
                Log.error("RestPageable.of() parse 'meta' element failed", e);
            }
        }
        return new RestPageable(compiledRoute, meta);
    }

    @Override
    public boolean hasNext() {
        if (this.metadata != null)
            return this.page != this.metadata.getPageTotal();
        else
            return false;
    }

    @Override
    public RestRoute.CompiledRoute next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return compiledRoute.withQueryParam("page", ++page);
    }

    public static class Metadata {
        private int page;
        private int pageTotal;
        private int pageSize;
        private int total;

        public Metadata() {
        }

        public int getPage() {
            return page;
        }

        public int getPageTotal() {
            return pageTotal;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotal() {
            return total;
        }
    }
}