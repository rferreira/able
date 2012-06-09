/*
 * Copyright 2011 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package net.lightbody.able.core.http;

import java.util.HashMap;
import java.util.Map;

/**
 * The request method of HTTP or its derived protocols, such as
 * <a href="http://en.wikipedia.org/wiki/Real_Time_Streaming_Protocol">RTSP</a> and
 * <a href="http://en.wikipedia.org/wiki/Internet_Content_Adaptation_Protocol">ICAP</a>.
 * @apiviz.exclude
 */
public class Methods implements Comparable<Methods> {
    /**
     * The OPTIONS method represents a request for information about the communication options available on the request/response
     * chain identified by the Request-URI. This method allows the client to determine the options and/or requirements
     * associated with a resource, or the capabilities of a server, without implying a resource action or initiating a
     * resource retrieval.
     */
    public static final Methods OPTIONS = new Methods("OPTIONS");

    /**
     * The GET method means retrieve whatever information (in the form of an entity) is identified by the Request-URI.
     * If the Request-URI refers to a data-producing process, it is the produced data which shall be returned as the entity
     * in the response and not the source text of the process, unless that text happens to be the output of the process.
     */
    public static final Methods GET = new Methods("GET");

    /**
     * The HEAD method is identical to GET except that the server MUST NOT return a message-body in the response.
     */
    public static final Methods HEAD = new Methods("HEAD");

    /**
     * The POST method is used to request that the origin server accept the entity enclosed in the request as a new
     * subordinate of the resource identified by the Request-URI in the Request-Line.
     */
    public static final Methods POST = new Methods("POST");

    /**
     * The PUT method requests that the enclosed entity be stored under the supplied Request-URI.
     */
    public static final Methods PUT = new Methods("PUT");

    /**
     * The PATCH method requests that a set of changes described in the
     * request entity be applied to the resource identified by the Request-URI.
     */
    public static final Methods PATCH = new Methods("PATCH");

    /**
     * The DELETE method requests that the origin server delete the resource identified by the Request-URI.
     */
    public static final Methods DELETE = new Methods("DELETE");

    /**
     * The TRACE method is used to invoke a remote, application-layer loop- back of the request message.
     */
    public static final Methods TRACE = new Methods("TRACE");

    /**
     * This specification reserves the method name CONNECT for use with a proxy that can dynamically switch to being a tunnel
     */
    public static final Methods CONNECT = new Methods("CONNECT");

    private static final Map<String, Methods> methodMap =
            new HashMap<String, Methods>();

    static {
        methodMap.put(OPTIONS.toString(), OPTIONS);
        methodMap.put(GET.toString(), GET);
        methodMap.put(HEAD.toString(), HEAD);
        methodMap.put(POST.toString(), POST);
        methodMap.put(PUT.toString(), PUT);
        methodMap.put(PATCH.toString(), PATCH);
        methodMap.put(DELETE.toString(), DELETE);
        methodMap.put(TRACE.toString(), TRACE);
        methodMap.put(CONNECT.toString(), CONNECT);
    }

    /**
     * Returns the {@link Methods} represented by the specified name.
     * If the specified name is a standard HTTP method name, a cached instance
     * will be returned.  Otherwise, a new instance will be returned.
     */
    public static Methods valueOf(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        name = name.trim().toUpperCase();
        if (name.length() == 0) {
            throw new IllegalArgumentException("empty name");
        }

        Methods result = methodMap.get(name);
        if (result != null) {
            return result;
        } else {
            return new Methods(name);
        }
    }

    private final String name;

    /**
     * Creates a new HTTP method with the specified name.  You will not need to
     * create a new method unless you are implementing a protocol derived from
     * HTTP, such as
     * <a href="http://en.wikipedia.org/wiki/Real_Time_Streaming_Protocol">RTSP</a> and
     * <a href="http://en.wikipedia.org/wiki/Internet_Content_Adaptation_Protocol">ICAP</a>
     */
    public Methods(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        name = name.trim().toUpperCase();
        if (name.length() == 0) {
            throw new IllegalArgumentException("empty name");
        }

        for (int i = 0; i < name.length(); i ++) {
            if (Character.isISOControl(name.charAt(i)) ||
                Character.isWhitespace(name.charAt(i))) {
                throw new IllegalArgumentException("invalid character in name");
            }
        }

        this.name = name;
    }

    /**
     * Returns the name of this method.
     */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Methods)) {
            return false;
        }

        Methods that = (Methods) o;
        return getName().equals(that.getName());
    }

    @Override
    public String toString() {
        return getName();
    }

    public int compareTo(Methods o) {
        return getName().compareTo(o.getName());
    }
}
