package com.lambdaworks.redis;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assume.*;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.junit.Test;

import io.netty.util.internal.SystemPropertyUtil;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 */
public class UnixDomainSocketTest {

    protected String key = "key";
    protected String value = "value";

    @Test
    public void standalone_Linux_x86_64_socket() throws Exception {

        linuxOnly();

        RedisURI redisURI = getSocketRedisUri();

        RedisClient redisClient = new RedisClient(redisURI);

        RedisConnection<String, String> connection = redisClient.connect();

        someRedisAction(connection);
        connection.close();

        redisClient.shutdown();
    }

    private void linuxOnly() {
        String osName = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
        assumeTrue("Only supported on Linux, your os is " + osName, osName.startsWith("linux"));
    }

    private RedisURI getSocketRedisUri() throws IOException {
        File file = new File("work/socket-6479").getCanonicalFile();
        return RedisURI.create(RedisURI.URI_SCHEME_REDIS_SOCKET + "://" + file.getCanonicalPath());
    }

    private RedisURI getSentinelSocketRedisUri() throws IOException {
        File file = new File("work/socket-26379").getCanonicalFile();
        return RedisURI.create(RedisURI.URI_SCHEME_REDIS_SOCKET + "://" + file.getCanonicalPath());
    }

    @Test
    public void sentinel_Linux_x86_64_socket() throws Exception {

        linuxOnly();

        RedisURI uri = new RedisURI();
        uri.getSentinels().add(getSentinelSocketRedisUri());
        uri.setSentinelMasterId("mymaster");

        RedisClient redisClient = new RedisClient(uri);

        RedisConnection<String, String> connection = redisClient.connect();

        someRedisAction(connection);

        connection.close();

        RedisSentinelAsyncConnection<String, String> sentinelConnection = redisClient.connectSentinelAsync();

        assertThat(sentinelConnection.ping().get()).isEqualTo("PONG");
        sentinelConnection.close();

        redisClient.shutdown();
    }

    @Test
    public void sentinel_Linux_x86_64_socket_and_inet() throws Exception {

        linuxOnly();

        RedisURI uri = new RedisURI();
        uri.getSentinels().add(getSentinelSocketRedisUri());
        uri.getSentinels().add(RedisURI.create(RedisURI.URI_SCHEME_REDIS + "://" + TestSettings.host() + ":26379"));
        uri.setSentinelMasterId("mymaster");

        RedisClient redisClient = new RedisClient(uri);

        try {
            redisClient.connect();
            fail("Missing validation exception");
        } catch (RedisConnectionException e) {
            assertThat(e).hasMessageContaining("You cannot mix unix domain socket and IP socket URI's");
        } finally {
            redisClient.shutdown();
        }

    }

    private void someRedisAction(RedisConnection<String, String> connection) {
        connection.set(key, value);
        String result = connection.get(key);

        assertThat(result).isEqualTo(value);
    }
}
