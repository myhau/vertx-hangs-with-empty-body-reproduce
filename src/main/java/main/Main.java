package main;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.core.http.HttpServerResponse;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        Vertx v = Vertx.newInstance(io.vertx.core.Vertx.vertx());

        v.createHttpServer(new HttpServerOptions().setPort(8080)).requestHandler(new Handler1()).listen(8080);
    }
}

class Handler1 implements Handler<HttpServerRequest> {

    public Handler1() {
    }

    @Override
    public void handle(HttpServerRequest request) {
        HttpServerResponse response = request.response();
        Flowable<Buffer> flowable = request.toFlowable();
        Observable
                .timer(1, TimeUnit.SECONDS)
                .doOnNext(x -> {

                    flowable.subscribe(
                            next -> {
                                System.out.println("Element" + next);
                            },
                            err -> {
                                System.out.println("Error:");
                                err.printStackTrace(System.out);
                                response.putHeader("Content-Length", "3");
                                response.write("Err");
                                response.setStatusCode(500);
                                response.end();
                            },
                            () -> {
                                System.out.println("Completed");
                                response.putHeader("Content-Length", "2");
                                response.write("Ok");
                                response.setStatusCode(200);
                                response.end();
                            }
                    );

                })
                .subscribe();

    }
}
