/* 
 * Copyright 2014 Smithsonian Institution.  
 *
 * Permission is granted to use, copy, modify,
 * and distribute this software and its documentation for educational, research
 * and non-profit purposes, without fee and without a signed licensing
 * agreement, provided that this notice, including the following two paragraphs,
 * appear in all copies, modifications and distributions.  For commercial
 * licensing, contact the Office of the Chief Information Officer, Smithsonian
 * Institution, 380 Herndon Parkway, MRC 1010, Herndon, VA. 20170, 202-633-5256.
 *  
 * This software and accompanying documentation is supplied "as is" without
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
 */
package edu.smithsonian.services.fedorarepo;

import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraCredentials;
import com.yourmediashelf.fedora.client.request.FedoraRequest;
import edu.smithsonian.services.fedorarepo.datastream.FedoraDatastreamEndpoint;
import edu.smithsonian.services.fedorarepo.ingest.FedoraIngestEnpoint;
import edu.smithsonian.services.fedorarepo.pid.FedoraPidEndpoint;
import edu.smithsonian.services.fedorarepo.relationship.FedoraRelationshipEndpoint;
import edu.smithsonian.services.fedorarepo.search.FedoraSearchEndpoint;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link FedoraEndpoint}.
 */
public class FedoraComponent extends DefaultComponent
{
    private static final Logger LOG = Logger.getLogger(FedoraComponent.class.getName());

    private FedoraSettings settings;

    //TODO: Use Spring to inject the FedoraClient
    private FedoraClient client;

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception
    {
        //TODO: Remove this or at least make it configurable
        if (client == null)
        {
            if (this.settings != null && this.settings.hasCredentials())
            {
                LOG.info(String.format("Settings found:: Host: %s Username: %s", this.settings.getHost().toExternalForm(), this.settings.getUsername()));
                client = new FedoraClient(settings.getCredentials());
            }
            else if (System.getProperty("si.fedora.user") != null && System.getProperty("si.fedora.password") != null)
            {
                String host = System.getProperty("si.fedora.host", "http://localhost:8080/fedora");
                String user = System.getProperty("si.fedora.user");
                String password = System.getProperty("si.fedora.password");

                LOG.info(String.format("Settings found:: Host: %s Username: %s", host, user));
                client = new FedoraClient(new FedoraCredentials(host, user, password));
            }
            else
            {
                LOG.warning("No Fedora Settings found! Using defautls [Host: http://localhost:8080/fedora]");
                client = new FedoraClient(new FedoraCredentials("http://localhost:8080/fedora", "fedoraAdmin", "password"));
            }

            FedoraRequest.setDefaultClient(client);
        }//end if

        Endpoint endpoint;
        if ("nextpid".equalsIgnoreCase(remaining))
        {
            endpoint = new FedoraPidEndpoint(uri, this);
        }//end if
        //TODO: Finish 'ingest' with params and options...
//        else if ("ingest".equalsIgnoreCase(remaining))
//        {
//            endpoint = new FedoraIngestEnpoint(uri, this);
//        }//end else if
        else if ("create".equalsIgnoreCase(remaining))
        {
            endpoint = new FedoraIngestEnpoint(uri, this, true);
        }
        else if ("datastream".equalsIgnoreCase(remaining))
        {
            endpoint = new FedoraDatastreamEndpoint(uri, this);
        }
        else if ("search".equalsIgnoreCase(remaining))
        {
            endpoint = new FedoraSearchEndpoint(uri, this);
        }
        else if ("hasRelationship".equalsIgnoreCase(remaining) || "hasConcept".equalsIgnoreCase(remaining))
        {
            FedoraRelationshipEndpoint temp = new FedoraRelationshipEndpoint(uri, this);
            temp.setNameSpace("info:fedora/");
            temp.setPredicate(String.format("info:fedora/fedora-system:def/relations-external#%s", remaining));

            endpoint = temp;
        }
//        else if ("find".equalsIgnoreCase(remaining))
        else
        {
            throw new UnsupportedOperationException("Could not create Fedora Endpoint for " + remaining);
        }//end else

        setProperties(endpoint, parameters);
        return endpoint;
    }

    //Needed???
    public FedoraClient getClient()
    {
        return client;
    }

    public FedoraSettings getSettings()
    {
        return settings;
    }

    public void setSettings(FedoraSettings settings)
    {
        this.settings = settings;
    }
}
