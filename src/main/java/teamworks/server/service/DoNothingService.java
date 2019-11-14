package teamworks.server.service;

import org.springframework.context.annotation.Profile;

@org.springframework.stereotype.Service
@Profile("default")
public class DoNothingService implements Service {
    @Override
    public void run() {

    }
}
