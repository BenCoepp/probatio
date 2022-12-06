package de.bencoepp.utils.daemon.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Component
public class Scheduler {
    @Scheduled(fixedRate = 10000)
    public void executeScheduler(){
        String[] list = new String[]{
                "de.bencoepp.utils.daemon.service.tasks.DoctorTask"
        };
        for (String scheduler : list) {
            Thread t1 = new Thread(new Runnable() {
                public void run() {
                    try {
                        Class<?> clazz = Class.forName(scheduler);
                        Constructor<?> constructor = clazz.getConstructor();
                        Object instance = constructor.newInstance();
                        Method method;
                        try {
                            method = instance.getClass().getMethod("execute");
                            method.invoke(instance);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }catch (Exception e){
                        System.out.println(e);
                    }

                }});
            t1.start();
        }
    }
}
