package com.hospira.theradoc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.utils.StringFilter;

/**
 * Class that listens on a port and simulates processing a request ...
 * 
 * @author robweaver
 */
public class ADTServer {

    private static final Logger logger = Logger.getLogger(ADTServer.class.getName());
    /**
     *
     */
    public static final String HOST = "localhost";
    /**
     *
     */
    public static final int PORT = 9999;

    private static int getPort(int defaultPort) {
        //grab port from environment, otherwise fall back to default port 9999
        String systemPort = System.getProperty("jersey.test.port");
        if (null != systemPort) {
            try {
                return Integer.parseInt(systemPort);
            } catch (NumberFormatException e) {
            }
        }
        return defaultPort;
    }

    /*
     *  Start the server ...
     *
     * @return
     * @throws IOException
     */
    protected static TCPNIOTransport startServer() throws IOException {
        
        // Create a FilterChain using FilterChainBuilder
        FilterChainBuilder filterChainBuilder = FilterChainBuilder.stateless();

        // Add TransportFilter, which is responsible
        // for reading and writing data to the connection
        filterChainBuilder.add(new TransportFilter());

        // StringFilter is responsible for Buffer <-> String conversion
        filterChainBuilder.add(new StringFilter(Charset.forName("UTF-8")));

        // EchoFilter is responsible for echoing received messages
        filterChainBuilder.add(new ADTWorker());
        
        // Create TCP transport
        TCPNIOTransport transport =
                TCPNIOTransportBuilder.newInstance().build();
        
        // Set the class to handle the messages ...
        transport.setProcessor(filterChainBuilder.build());
        
        logger.info("Starting grizzly2...");

        // binding transport to start listen on certain host and port
        transport.bind(HOST, PORT);
        
        transport.start();
        return transport;
    }

    /**
     * Main method - allows running server from command line ...
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {


        // Grizzly 2 initialization
        TCPNIOTransport server = startServer();
        server.start();
        logger.info(String.format("Server listening on port %s",
                PORT));
        System.in.read();
        server.stop();
    }
}
