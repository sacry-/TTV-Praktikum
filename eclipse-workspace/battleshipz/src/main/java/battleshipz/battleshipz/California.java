package battleshipz.battleshipz;

import de.uniba.wiai.lspi.util.logging.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.net.URI;
import java.net.URISyntaxException;

public class California {
    private static Logger log = Logger.getLogger(California.class);
    private URI uri = null;
    private CoapClient client = null;
    
    public California() {
		try {
			this.uri = new URI("coap://localhost/led");
			this.client = new CoapClient(uri);
		}catch(URISyntaxException e) {
			log.error(e);
		}
        setLED(1);
    }
    
    public void setLED(int status){
        try {
            if (status > 0.5) { // green
                client.put("0", MediaTypeRegistry.TEXT_PLAIN);
                client.put("g", MediaTypeRegistry.TEXT_PLAIN);
            } else if (status <= 0.5 && status > 0) { // violet
                client.put("1", MediaTypeRegistry.TEXT_PLAIN);
                client.put("g", MediaTypeRegistry.TEXT_PLAIN);
            } else if (status == 0) { // red
                client.put("0", MediaTypeRegistry.TEXT_PLAIN);
                client.put("r", MediaTypeRegistry.TEXT_PLAIN);
            } else {
            		log.error("Unknown LED status.");
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}