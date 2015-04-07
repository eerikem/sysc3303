package common;

import java.io.IOException;

public interface EventHandler {
	public boolean handleEvent(Event e) throws IOException;
}
