package lib.types;

import java.io.IOException;

public interface HttpResponse {
    void send(Object data) throws IOException;
}
