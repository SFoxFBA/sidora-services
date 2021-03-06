/*
 * Copyright 2015-2016 Smithsonian Institution.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.You may obtain a copy of
 * the License at: http://www.apache.org/licenses/
 *
 * This software and accompanying documentation is supplied without
 * warranty of any kind. The copyright holder and the Smithsonian Institution:
 * (1) expressly disclaim any warranties, express or implied, including but not
 * limited to any implied warranties of merchantability, fitness for a
 * particular purpose, title or non-infringement; (2) do not assume any legal
 * liability or responsibility for the accuracy, completeness, or usefulness of
 * the software; (3) do not represent that use of the software would not
 * infringe privately owned rights; (4) do not warrant that the software
 * is error-free or will be maintained, supported, updated or enhanced;
 * (5) will not be liable for any indirect, incidental, consequential special
 * or punitive damages of any kind or nature, including but not limited to lost
 * profits or loss of data, on any basis arising from contract, tort or
 * otherwise, even if any of the parties has been warned of the possibility of
 * such loss or damage.
 *
 * This distribution includes several third-party libraries, each with their own
 * license terms. For a complete copy of all copyright and license terms, including
 * those of third-party libraries, please see the product release notes.
 */

package edu.si.services.fedorarepo.datastream;

import edu.si.services.fedorarepo.FedoraComponent;
import org.apache.camel.Producer;
import org.apache.camel.spi.UriParam;

/**
 *
 * @author davisda
 */
public class FedoraGetDatastreamEndpoint extends FedoraDatastreamEndpoint
{
    @UriParam
    private String dsId;
    @UriParam
    private String asOfDateTime;

    public FedoraGetDatastreamEndpoint(String uri, FedoraComponent component)
    {
        super(uri, component);
    }

    @Override
    public Producer createProducer() throws Exception
    {
        return new FedoraGetDatastreamProducer(this);
    }

    public String getDsId()
    {
        return dsId;
    }

    public void setDsId(String dsId)
    {
        this.dsId = dsId;
    }

    public String getAsOfDateTime()
    {
        return asOfDateTime;
    }

    public void setAsOfDateTime(String asOfDateTime)
    {
        this.asOfDateTime = asOfDateTime;
    }
}
