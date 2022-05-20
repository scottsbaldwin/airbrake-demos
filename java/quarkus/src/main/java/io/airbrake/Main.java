package io.airbrake;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.airbrake.javabrake.Config;
import io.airbrake.javabrake.Notice;
import io.airbrake.javabrake.Notifier;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {
        @ConfigProperty(name = "airbrake.project.id")
        int airbrakeProjectId;
        @ConfigProperty(name = "airbrake.project.key")
        String airbrakeProjectKey;

        public static Notifier Notifier;

        @Override
        public int run(String... args) throws Exception {
            initializeAirbrake();
            Quarkus.waitForExit();
            return 0;
        }

        private void initializeAirbrake() {
            Config config = new Config();
            config.projectId = airbrakeProjectId;
            config.projectKey = airbrakeProjectKey;
            MyApp.Notifier = new Notifier(config);

            MyApp.Notifier.addFilter(
                    (Notice notice) -> {
                        notice.setContext("environment", "development");
                        return notice;
                    });
        }

    }
}
