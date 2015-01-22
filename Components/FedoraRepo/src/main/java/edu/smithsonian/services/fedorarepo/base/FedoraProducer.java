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
package edu.smithsonian.services.fedorarepo.base;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultProducer;

/**
 *
 * @author jshingler
 */
public abstract class FedoraProducer extends DefaultProducer
{

    public FedoraProducer(Endpoint endpoint)
    {
        super(endpoint);
    }

    protected boolean hasParam(String param)
    {
        //NOTE: This does not call param.trim() prior to checking if it is empty
        return (param != null && !param.isEmpty() && !"null".equalsIgnoreCase(param));
    }

    protected String getParam(String param)
    {
        if (hasParam(param))
        {
            return param;
        }//end if
        else
        {
            return null;
        }//end else
    }

    protected String getParam(String param, String header)
    {
        String value = null;
        if (hasParam(param))
        {
            value = param;
        }//end if
        else if (!"null".equalsIgnoreCase(param))
        {
            value = header;
        }//end else

        return value;
    }

//    protected boolean checkStatus(FedoraResponse response)
//    {
//        //Think checking the status is pointless since the FedoraClient will throw
//        // an exception if the execution failed
//        return (response.getStatus() >= 200 && response.getStatus() < 300);
//    }
}
