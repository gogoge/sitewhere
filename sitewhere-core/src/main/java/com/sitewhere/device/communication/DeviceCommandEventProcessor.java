/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.device.communication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.sitewhere.SiteWhere;
import com.sitewhere.device.event.processor.OutboundEventProcessor;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.device.batch.IBatchOperation;
import com.sitewhere.spi.device.event.IDeviceCommandInvocation;

/**
 * Event processor that hands off {@link IDeviceCommandInvocation} events after they have
 * been saved so that the communication subsystem can process them.
 * 
 * @author Derek
 */
public class DeviceCommandEventProcessor extends OutboundEventProcessor {

	/** Static logger instance */
	private static Logger LOGGER = Logger.getLogger(DeviceCommandEventProcessor.class);

	/** Number of invocations to buffer before blocking calls */
	private static final int DEFAULT_NUM_THREADS = 10;

	/** Number of threads used for processing command requests */
	private int numThreads = DEFAULT_NUM_THREADS;

	/** Used to execute Solr indexing in a separate thread */
	private ExecutorService executor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.rest.model.device.event.processor.OutboundEventProcessor#start()
	 */
	@Override
	public void start() throws SiteWhereException {
		LOGGER.info("Command event processor using " + getNumThreads() + " threads to process requests.");
		executor = Executors.newFixedThreadPool(getNumThreads(), new ProcessorsThreadFactory());
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
	 * @see com.sitewhere.rest.model.device.event.processor.OutboundEventProcessor#stop()
	 */
	@Override
	public void stop() throws SiteWhereException {
		executor.shutdownNow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.rest.model.device.event.processor.OutboundEventProcessor#
	 * onCommandInvocation(com.sitewhere.spi.device.event.IDeviceCommandInvocation)
	 */
	@Override
	public void onCommandInvocation(IDeviceCommandInvocation invocation) throws SiteWhereException {
		executor.execute(new CommandInvocationProcessor(invocation));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.device.event.processor.OutboundEventProcessor#onBatchOperation(com
	 * .sitewhere.spi.device.batch.IBatchOperation)
	 */
	@Override
	public void onBatchOperation(IBatchOperation operation) throws SiteWhereException {
		executor.execute(new BatchOperationProcessor(operation));
	}

	/**
	 * Processes command invocations asynchronously.
	 */
	private class CommandInvocationProcessor implements Runnable {

		private IDeviceCommandInvocation command;

		public CommandInvocationProcessor(IDeviceCommandInvocation command) {
			this.command = command;
		}

		@Override
		public void run() {
			try {
				LOGGER.debug("Command processor thread processing command invocation.");
				SiteWhere.getServer().getDeviceCommunicationSubsystem().deliverCommand(command);
			} catch (SiteWhereException e) {
				LOGGER.error("Exception thrown in command processing operation.", e);
			} catch (Throwable e) {
				LOGGER.error("Unhandled exception in command processing operation.", e);
			}
		}
	}

	/**
	 * Processes batch operations asynchronously.
	 */
	private class BatchOperationProcessor implements Runnable {

		private IBatchOperation operation;

		public BatchOperationProcessor(IBatchOperation operation) {
			this.operation = operation;
		}

		@Override
		public void run() {
			try {
				LOGGER.debug("Command processor thread processing batch operation.");
				SiteWhere.getServer().getDeviceCommunicationSubsystem().getBatchOperationManager().process(
						operation);
			} catch (SiteWhereException e) {
				LOGGER.error("Exception thrown in batch processing operation.", e);
			} catch (Throwable e) {
				LOGGER.error("Unhandled exception in batch processing operation.", e);
			}
		}
	}

	/** Used for naming processor threads */
	private class ProcessorsThreadFactory implements ThreadFactory {

		/** Counts threads */
		private AtomicInteger counter = new AtomicInteger();

		public Thread newThread(Runnable r) {
			return new Thread(r, "Command processor " + counter.incrementAndGet());
		}
	}

	public int getNumThreads() {
		return numThreads;
	}

	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}
}