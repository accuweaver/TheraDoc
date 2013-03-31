/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hospira.theradoc;

import java.io.IOException;
import java.util.logging.Logger;

import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChain;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;

/**
 * Implementation of {@link FilterChain} filter, which replies with the request
 * message.
 */
public class EchoFilter extends BaseFilter {
    /** 
     * Logger for this class ...
     */
    private static final Logger logger = Logger.getLogger(EchoFilter.class.getName());

    /**
     * Handle just read operation, when some message has come and ready to be
     * processed.
     *
     * @param ctx Context of {@link FilterChainContext} processing
     * @return the next action
     * @throws java.io.IOException
     */
    @Override
    public NextAction handleRead(FilterChainContext ctx)
            throws IOException {

        // Peer address is used for non-connected UDP Connection
        final Object peerAddress = ctx.getAddress();

        final Object message = ctx.getMessage();

        logger.info("Connection established and stuff...");

        ctx.write(peerAddress, message, null);

        return ctx.getStopAction();
    }
}
