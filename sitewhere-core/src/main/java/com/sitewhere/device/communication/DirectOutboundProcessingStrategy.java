/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.device.communication;

import org.apache.log4j.Logger;

import com.sitewhere.SiteWhere;
import com.sitewhere.server.lifecycle.LifecycleComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.device.batch.IBatchOperation;
import com.sitewhere.spi.device.communication.IOutboundProcessingStrategy;
import com.sitewhere.spi.device.event.IDeviceAlert;
import com.sitewhere.spi.device.event.IDeviceCommandInvocation;
import com.sitewhere.spi.device.event.IDeviceCommandResponse;
import com.sitewhere.spi.device.event.IDeviceLocation;
import com.sitewhere.spi.device.event.IDeviceMeasurements;
import com.sitewhere.spi.device.event.processor.IOutboundEventProcessorChain;
import com.sitewhere.spi.server.lifecycle.LifecycleComponentType;

/**
 * Implementation of {@link IOutboundProcessingStrategy} that sends messages directly to
 * the {@link IOutboundEventProcessorChain}.
 * 
 * @author Derek
 */
public class DirectOutboundProcessingStrategy extends LifecycleComponent implements
		IOutboundProcessingStrategy {

	/** Static logger instance */
	private static Logger LOGGER = Logger.getLogger(DirectOutboundProcessingStrategy.class);

	public DirectOutboundProcessingStrategy() {
		super(LifecycleComponentType.OutboundProcessingStrategy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#start()
	 */
	@Override
	public void start() throws SiteWhereException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
	 */
	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#stop()
	 */
	@Override
	public void stop() throws SiteWhereException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.spi.device.event.processor.IOutboundEventProcessor#onMeasurements
	 * (com.sitewhere.spi.device.event.IDeviceMeasurements)
	 */
	@Override
	public void onMeasurements(IDeviceMeasurements measurements) throws SiteWhereException {
		SiteWhere.getServer().getOutboundEventProcessorChain().onMeasurements(measurements);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.spi.device.event.processor.IOutboundEventProcessor#onLocation(com
	 * .sitewhere.spi.device.event.IDeviceLocation)
	 */
	@Override
	public void onLocation(IDeviceLocation location) throws SiteWhereException {
		SiteWhere.getServer().getOutboundEventProcessorChain().onLocation(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.spi.device.event.processor.IOutboundEventProcessor#onAlert(com.sitewhere
	 * .spi.device.event.IDeviceAlert)
	 */
	@Override
	public void onAlert(IDeviceAlert alert) throws SiteWhereException {
		SiteWhere.getServer().getOutboundEventProcessorChain().onAlert(alert);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.spi.device.event.processor.IOutboundEventProcessor#onCommandInvocation
	 * (com.sitewhere.spi.device.event.IDeviceCommandInvocation)
	 */
	@Override
	public void onCommandInvocation(IDeviceCommandInvocation invocation) throws SiteWhereException {
		SiteWhere.getServer().getOutboundEventProcessorChain().onCommandInvocation(invocation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.spi.device.event.processor.IOutboundEventProcessor#onCommandResponse
	 * (com.sitewhere.spi.device.event.IDeviceCommandResponse)
	 */
	@Override
	public void onCommandResponse(IDeviceCommandResponse response) throws SiteWhereException {
		SiteWhere.getServer().getOutboundEventProcessorChain().onCommandResponse(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.spi.device.event.processor.IOutboundEventProcessor#onBatchOperation
	 * (com.sitewhere.spi.device.batch.IBatchOperation)
	 */
	@Override
	public void onBatchOperation(IBatchOperation operation) throws SiteWhereException {
		SiteWhere.getServer().getOutboundEventProcessorChain().onBatchOperation(operation);
	}
}