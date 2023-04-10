package com.denisfesenko;

import com.denisfesenko.service.DanasService;
import com.denisfesenko.service.OpenAiService;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

public class TgSerbianNewsBot {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        new OpenAiService().translate(DanasService.getNews()).forEach(pair -> System.out.println(pair.getLeft() + " " + pair.getRight()));
        stopWatch.stop();
        System.out.println("Elapsed Time in seconds: "+ stopWatch.getTime(TimeUnit.SECONDS));
    }
}
