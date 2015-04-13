/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.spi.device.communication;

import java.util.List;

import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.device.batch.IBatchOperationManager;
import com.sitewhere.spi.device.command.ISystemCommand;
import com.sitewhere.spi.device.event.IDeviceCommandInvocation;
import com.sitewhere.spi.server.lifecycle.ILifecycleComponent;

/**
 * Base interface for system components that empower device communication.
 * 
 * @author Derek
 */
public interface IDeviceCommunication extends ILifecycleComponent {

	/**
	 * Get the list of sources that bring device event data into the system.
	 * 
	 * @return
	 */
	public List<IInboundEventSource<?>> getInboundEventSources();

	/**
	 * Get the strategy for moving decoded events into the inbound chain.
	 * 
	 * @return
	 */
	public IInboundProcessingStrategy getInboundProcessingStrategy();

	/**
	 * Get the configured registration manager.
	 * 
	 * @return
	 */
	public IRegistrationManager getRegistrationManager();

	/**
	 * Get the configured batch operation manager.
	 * 
	 * @return
	 */
	public IBatchOperationManager getBatchOperationManager();

	/**
	 * Get the strategy for moving processed events into the outbound chain.
	 * 
	 * @return
	 */
	public IOutboundProcessingStrategy getOutboundProcessingStrategy();

	/**
	 * Get the command processing strategy.
	 * 
	 * @return
	 */
	public ICommandProcessingStrategy getCommandProcessingStrategy();

	/**
	 * Get the router that chooses which destination will process a command.
	 * 
	 * @return
	 */
	public IOutboundCommandRouter getOutboundCommandRouter();

	/**
	 * Get the list of command destinations that can deliver commands to devices.
	 * 
	 * @return
	 */
	public List<ICommandDestination<?, ?>> getCommandDestinations();

	/**
	 * Deliver a command invocation.
	 * 
	 * @param invocation
	 * @throws SiteWhereException
	 */
	public void deliverCommand(IDeviceCommandInvocation invocation) throws SiteWhereException;

	/**
	 * Deliver a system command.
	 * 
	 * @param hardwareId
	 * @param command
	 * @throws SiteWhereException
	 */
	public void deliverSystemCommand(String hardwareId, ISystemCommand command) throws SiteWhereException;
}