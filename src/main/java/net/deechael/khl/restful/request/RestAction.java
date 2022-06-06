package net.deechael.khl.restful.request;

import net.deechael.khl.RabbitImpl;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.core.RabbitObject;

import java.util.concurrent.ExecutionException;

public abstract class RestAction extends RabbitObject {

    public RestAction(RabbitImpl rabbit) {
        super(rabbit);
    }

    public RestFuture submit() {
        return new RestFuture();
    }

    public HttpCall.Response execute() throws ExecutionException, InterruptedException {
        return submit().get();
    }

}
