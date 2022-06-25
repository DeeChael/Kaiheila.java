package net.deechael.khl.client.http;

import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.restful.RestRoute;
import okhttp3.FormBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBuilder extends KaiheilaObject {
  protected final Logger Log = LoggerFactory.getLogger(this.getClass());
  RestRoute.CompiledRoute route;
  FormBody.Builder formBuilder;
  protected final HttpHeaders headers = new HttpHeaders();

  private RequestBuilder(KaiheilaBot rabbit, RestRoute.CompiledRoute route) {
    super(rabbit);
    this.route = route;
    if (getKaiheilaBot().getConfiguration().getApiConfigurer().getToken().isEmpty()){
      Log.warn("Token is empty");
    }
    headers.addHeader("Authorization", "Bot " + getKaiheilaBot().getConfiguration().getApiConfigurer().getToken());
  }

  public static RequestBuilder create(KaiheilaBot rabbit, RestRoute route) {
    return new RequestBuilder(rabbit, route.compile());
  }

  public static RequestBuilder create(KaiheilaBot rabbit, RestRoute.CompiledRoute route) {
    return new RequestBuilder(rabbit, route);
  }

  public RequestBuilder withData(String key, Object value) {
    if (value == null) return this;
    if (formBuilder == null) {
      formBuilder = new FormBody.Builder();
    }
    formBuilder.add(key, String.valueOf(value));
    return this;
  }

  public RequestBuilder withQuery(String key, String value) {
    route.withQueryParam(key, value);
    return this;
  }

  public HttpCall build() {
    return formBuilder != null ? HttpCall.createRequest(getKaiheilaBot().getConfiguration().getApiConfigurer().getBaseUrl() + route.getQueryStringCompleteRoute(), headers, formBuilder.build()) :
            HttpCall.createRequest(route.getMethod(), getKaiheilaBot().getConfiguration().getApiConfigurer().getBaseUrl() + route.getQueryStringCompleteRoute(), headers);
  }
}
