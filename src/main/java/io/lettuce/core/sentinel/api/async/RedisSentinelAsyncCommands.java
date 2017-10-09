/*
 * Copyright 2011-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.sentinel.api.async;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import io.lettuce.core.KillArgs;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import io.lettuce.core.RedisFuture;

/**
 * Asynchronous executed commands for Redis Sentinel.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Mark Paluch
 * @since 4.0
 * @generated by io.lettuce.apigenerator.CreateAsyncApi
 */
public interface RedisSentinelAsyncCommands<K, V> {

    /**
     * Return the ip and port number of the master with that name.
     *
     * @param key the key
     * @return SocketAddress;
     */
    RedisFuture<SocketAddress> getMasterAddrByName(K key);

    /**
     * Enumerates all the monitored masters and their states.
     *
     * @return Map&lt;K, V&gt;&gt;
     */
    RedisFuture<List<Map<K, V>>> masters();

    /**
     * Show the state and info of the specified master.
     *
     * @param key the key
     * @return Map&lt;K, V&gt;
     */
    RedisFuture<Map<K, V>> master(K key);

    /**
     * Provides a list of slaves for the master with the specified name.
     *
     * @param key the key
     * @return List&lt;Map&lt;K, V&gt;&gt;
     */
    RedisFuture<List<Map<K, V>>> slaves(K key);

    /**
     * This command will reset all the masters with matching name.
     *
     * @param key the key
     * @return Long
     */
    RedisFuture<Long> reset(K key);

    /**
     * Perform a failover.
     *
     * @param key the master id
     * @return String
     */
    RedisFuture<String> failover(K key);

    /**
     * This command tells the Sentinel to start monitoring a new master with the specified name, ip, port, and quorum.
     *
     * @param key the key
     * @param ip the IP address
     * @param port the port
     * @param quorum the quorum count
     * @return String
     */
    RedisFuture<String> monitor(K key, String ip, int port, int quorum);

    /**
     * Multiple option / value pairs can be specified (or none at all).
     *
     * @param key the key
     * @param option the option
     * @param value the value
     *
     * @return String simple-string-reply {@code OK} if {@code SET} was executed correctly.
     */
    RedisFuture<String> set(K key, String option, V value);

    /**
     * remove the specified master.
     *
     * @param key the key
     * @return String
     */
    RedisFuture<String> remove(K key);

    /**
     * Get the current connection name.
     *
     * @return K bulk-string-reply The connection name, or a null bulk reply if no name is set.
     */
    RedisFuture<K> clientGetname();

    /**
     * Set the current connection name.
     *
     * @param name the client name
     * @return simple-string-reply {@code OK} if the connection name was successfully set.
     */
    RedisFuture<String> clientSetname(K name);

    /**
     * Kill the connection of a client identified by ip:port.
     *
     * @param addr ip:port
     * @return String simple-string-reply {@code OK} if the connection exists and has been closed
     */
    RedisFuture<String> clientKill(String addr);

    /**
     * Kill connections of clients which are filtered by {@code killArgs}
     *
     * @param killArgs args for the kill operation
     * @return Long integer-reply number of killed connections
     */
    RedisFuture<Long> clientKill(KillArgs killArgs);

    /**
     * Stop processing commands from clients for some time.
     *
     * @param timeout the timeout value in milliseconds
     * @return String simple-string-reply The command returns OK or an error if the timeout is invalid.
     */
    RedisFuture<String> clientPause(long timeout);

    /**
     * Get the list of client connections.
     *
     * @return String bulk-string-reply a unique string, formatted as follows: One client connection per line (separated by LF),
     *         each line is composed of a succession of property=value fields separated by a space character.
     */
    RedisFuture<String> clientList();

    /**
     * Get information and statistics about the server.
     *
     * @return String bulk-string-reply as a collection of text lines.
     */
    RedisFuture<String> info();

    /**
     * Get information and statistics about the server.
     *
     * @param section the section type: string
     * @return String bulk-string-reply as a collection of text lines.
     */
    RedisFuture<String> info(String section);

    /**
     * Ping the server.
     *
     * @return String simple-string-reply
     */
    RedisFuture<String> ping();

    /**
     *
     * @return true if the connection is open (connected and not closed).
     */
    boolean isOpen();

    /**
     *
     * @return the underlying connection.
     */
    StatefulRedisSentinelConnection<K, V> getStatefulConnection();
}