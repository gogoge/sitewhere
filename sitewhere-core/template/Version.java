/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere;

import com.sitewhere.server.SiteWhereServer;
import com.sitewhere.spi.server.ISiteWhereServer;
import com.sitewhere.spi.system.IVersion;

/**
 * Used as basis for generating version information. This file is modified by the Maven
 * build process so that the correct values exist in the compiled classes.
 * 
 * @author Derek
 */
public class Version implements IVersion {

	/** Version identifier supplied by the Maven POM */
	public static final String VERSION_IDENTIFIER = "@version.identifier@";

	/** Timestamp for build */
	public static final String BUILD_TIMESTAMP = "@build.timestamp@";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.spi.system.IVersion#getEdition()
	 */
	public String getEdition() {
		return "Community Edition";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.spi.system.IVersion#getEditionIdentifier()
	 */
	public String getEditionIdentifier() {
		return "CE";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.spi.system.IVersion#getVersionIdentifier()
	 */
	public String getVersionIdentifier() {
		return VERSION_IDENTIFIER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.spi.system.IVersion#getBuildTimestamp()
	 */
	public String getBuildTimestamp() {
		return BUILD_TIMESTAMP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sitewhere.spi.system.IVersion#getServerClass()
	 */
	public Class<? extends ISiteWhereServer> getServerClass() {
		return SiteWhereServer.class;
	}
}