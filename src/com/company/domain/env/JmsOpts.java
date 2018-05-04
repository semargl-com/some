package com.company.domain.env;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JmsOpts {

    public JmsOpts() {
        System.out.println(getJvmOpts());
    }

    private static Map<String, String> getJvmOpts() {
        //StringBuilder sb = new StringBuilder();
        //RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        //List<String> arguments = runtimeMxBean.getInputArguments();
        List<String> arguments = Arrays.asList(
                "-Dakka.remote.netty.tcp.port=5000",
                "-Dakka.remote.enabled-transports.0=akka.remote.netty.tcp",
                "-Dakka.remote.netty.tcp.hostname=",
                "-Xms2G",
                "-Dakka.cluster.seed-nodes.24=akka.tcp://application@172.17.0.7:5000"
        );
        return arguments.stream().collect(Collectors.toMap(extractJvmKeyFunction, extractJvmValueFunction));
    }

    static Function<String, String> extractJvmKeyFunction = (s) -> {
        if (s.startsWith("-D") && s.contains("=")) {
            return s.substring(2, s.indexOf("="));
        }
        return s;
    };

    static Function<String, String> extractJvmValueFunction = (s) -> {
        if (s.startsWith("-D") && s.contains("=")) {
            return s.substring(s.indexOf("=") + 1);
        }
        return s;
    };
}
