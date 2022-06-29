package net.deechael.khl.restful.request;

import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.gate.Gateway;

import java.util.concurrent.ExecutionException;

public abstract class RestAction extends KaiheilaObject {

    public RestAction(Gateway gateway) {
        super(gateway);
    }

    public RestFuture submit() {
        return new RestFuture();
    }

    public HttpCall.Response execute() throws ExecutionException, InterruptedException {
        return submit().get();
    }

}
