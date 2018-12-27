package com.niveka_team_dev.prospectingapp.utilities;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LogJsonInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        request = request.newBuilder()
                //.addHeader("accept","application/json; charset=utf-8")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                //.addHeader("Content-Type", "application/text; charset=utf-8")
                .build();

        Response response = chain.proceed(request);
        String rawJson = response.body().string();

        //BufferedReader in = null;
        //in = new BufferedReader(new InputStreamReader(request.body().contentType()));

        //Log.e("LogJsonInterceptor", String.format("raw JSON response is: %s",rawJson));
        // Re-create the response before returning it because body can be read only once
        return response.newBuilder()
                .body(ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), rawJson)).build();

        //return request.newBuilder().build();
    }

    public String bodyToString(final RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if(copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }
}
