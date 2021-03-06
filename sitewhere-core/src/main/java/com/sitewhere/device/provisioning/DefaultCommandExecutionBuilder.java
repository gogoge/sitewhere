/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.device.provisioning;

import org.apache.log4j.Logger;

import com.sitewhere.rest.model.device.command.DeviceCommandExecution;
import com.sitewhere.server.lifecycle.LifecycleComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.SiteWhereSystemException;
import com.sitewhere.spi.device.command.ICommandParameter;
import com.sitewhere.spi.device.command.IDeviceCommand;
import com.sitewhere.spi.device.command.IDeviceCommandExecution;
import com.sitewhere.spi.device.event.IDeviceCommandInvocation;
import com.sitewhere.spi.device.provisioning.ICommandExecutionBuilder;
import com.sitewhere.spi.error.ErrorCode;
import com.sitewhere.spi.error.ErrorLevel;
import com.sitewhere.spi.server.lifecycle.LifecycleComponentType;

/**
 * Default implementation of the {@link ICommandExecutionBuilder} interface that handles
 * the basic task of merging {@link IDeviceCommand} and {@link IDeviceCommandInvocation}
 * information to produce an {@link IDeviceCommandExecution} that can be encoded and sent
 * to a target.
 * 
 * @author Derek
 */
public class DefaultCommandExecutionBuilder extends LifecycleComponent implements ICommandExecutionBuilder {

	/** Static logger instance */
	private static Logger LOGGER = Logger.getLogger(DefaultCommandExecutionBuilder.class);

	public DefaultCommandExecutionBuilder() {
		super(LifecycleComponentType.CommandExecutionBuilder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sitewhere.spi.device.provisioning.ICommandExecutionBuilder#createExecution(
	 * com.sitewhere.spi.device.command.IDeviceCommand,
	 * com.sitewhere.spi.device.event.IDeviceCommandInvocation)
	 */
	@Override
	public IDeviceCommandExecution createExecution(IDeviceCommand command, IDeviceCommandInvocation invocation)
			throws SiteWhereException {
		LOGGER.debug("Building default command execution for invocation.");
		DeviceCommandExecution execution = new DeviceCommandExecution();
		execution.setCommand(command);
		execution.setInvocation(invocation);
		generateParameters(execution);
		return execution;
	}

	/**
	 * Generate a parameters map based on information from the command and invocation.
	 * 
	 * @param execution
	 * @throws SiteWhereException
	 */
	protected void generateParameters(IDeviceCommandExecution execution) throws SiteWhereException {
		execution.getParameters().clear();
		for (ICommandParameter parameter : execution.getCommand().getParameters()) {
			String paramValue = execution.getInvocation().getParameterValues().get(parameter.getName());
			if (parameter.isRequired() && (paramValue == null)) {
				throw new SiteWhereSystemException(ErrorCode.RequiredCommandParameterMissing,
						ErrorLevel.ERROR);
			}
			Object converted = null;
			switch (parameter.getType()) {
			case Bool: {
				converted = Boolean.parseBoolean(paramValue);
				break;
			}
			case String: {
				converted = paramValue;
				break;
			}
			case Bytes: {
				converted = String.valueOf(converted).getBytes();
				break;
			}
			case Double: {
				try {
					converted = Double.parseDouble(paramValue);
				} catch (NumberFormatException e) {
					throw new SiteWhereException("Field '" + parameter.getName()
							+ "' contains a value that can not be parsed as a double.");
				}
				break;
			}
			case Float: {
				try {
					converted = Float.parseFloat(paramValue);
				} catch (NumberFormatException e) {
					throw new SiteWhereException("Field '" + parameter.getName()
							+ "' contains a value that can not be parsed as a float.");
				}
				break;
			}
			case Int32:
			case UInt32:
			case SInt32:
			case Fixed32:
			case SFixed32: {
				try {
					converted = Integer.parseInt(paramValue);
				} catch (NumberFormatException e) {
					throw new SiteWhereException("Field '" + parameter.getName()
							+ "' contains a value that can not be parsed as an integer.");
				}
				break;
			}
			case Int64:
			case UInt64:
			case SInt64:
			case Fixed64:
			case SFixed64: {
				try {
					converted = Long.parseLong(paramValue);
				} catch (NumberFormatException e) {
					throw new SiteWhereException("Field '" + parameter.getName()
							+ "' contains a value that can not be parsed as an long.");
				}
				break;
			}
			default: {
				throw new SiteWhereException("Unhandled parameter type: " + parameter.getType().name());
			}

			}
			if (converted != null) {
				execution.getParameters().put(parameter.getName(), converted);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#start()
	 */
	@Override
	public void start() throws SiteWhereException {
		LOGGER.info("Started command execution builder.");
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
		LOGGER.info("Stopped command execution builder.");
	}
}