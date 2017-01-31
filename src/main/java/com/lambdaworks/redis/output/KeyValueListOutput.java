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
package com.lambdaworks.redis.output;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lambdaworks.redis.KeyValue;
import com.lambdaworks.redis.codec.RedisCodec;
import com.lambdaworks.redis.internal.LettuceAssert;

/**
 * {@link List} of values output.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 *
 * @author Mark Paluch
 */
public class KeyValueListOutput<K, V> extends CommandOutput<K, V, List<KeyValue<K, V>>>
        implements StreamingOutput<KeyValue<K, V>> {

    private Subscriber<KeyValue<K, V>> subscriber;
    private Iterable<K> keys;
    private Iterator<K> keyIterator;

    public KeyValueListOutput(RedisCodec<K, V> codec, Iterable<K> keys) {
        super(codec, new ArrayList<>());
        setSubscriber(ListSubscriber.of(output));
        this.keys = keys;
    }

    @Override
    public void set(ByteBuffer bytes) {

        if(keyIterator == null) {
            keyIterator = keys.iterator();
        }

        subscriber.onNext(KeyValue.fromNullable(keyIterator.next(), bytes == null ? null : codec.decodeValue(bytes)));
    }

    @Override
    public void setSubscriber(Subscriber<KeyValue<K, V>> subscriber) {
        LettuceAssert.notNull(subscriber, "Subscriber must not be null");
        this.subscriber = subscriber;
    }

    @Override
    public Subscriber<KeyValue<K, V>> getSubscriber() {
        return subscriber;
    }
}