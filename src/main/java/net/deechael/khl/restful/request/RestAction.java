package net.deechael.khl.restful.request;

import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.core.KaiheilaObject;

import java.util.concurrent.ExecutionException;

public abstract class RestAction extends KaiheilaObject {

    public RestAction(KaiheilaBot rabbit) {
        super(rabbit);
    }

    public RestFuture submit() {
        return new RestFuture();
    }

    public HttpCall.Response execute() throws ExecutionException, InterruptedException {
        return submit().get();
    }

}
