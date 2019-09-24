/*-
 * #%L
 * Elastic APM Java agent
 * %%
 * Copyright (C) 2018 - 2019 Elastic and contributors
 * %%
 * Licensed to Elasticsearch B.V. under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch B.V. licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * #L%
 */
package co.elastic.apm.agent.camel;

import co.elastic.apm.agent.bci.ElasticApmInstrumentation;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.camel.Exchange;

import java.util.Arrays;
import java.util.Collection;

import static net.bytebuddy.matcher.ElementMatchers.hasSuperType;
import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Created by Lukasz on 2019-09-24.
 */
public class ApacheCamelProcessInstrumentation extends ElasticApmInstrumentation{
    @Override
    public ElementMatcher<? super TypeDescription> getTypeMatcher() {
        return hasSuperType(named("org.apache.camel.Processor"));
    }

    @Override
    public ElementMatcher<? super MethodDescription> getMethodMatcher() {
        return named("process");
    }

    @Override
    public Collection<String> getInstrumentationGroupNames() {
        return Arrays.asList("apache-camel");
    }

    @Override
    public Class<?> getAdviceClass() {
        return ApacheCamelProcessAdvice.class;
    }


    private static class ApacheCamelProcessAdvice {
        @Advice.OnMethodEnter(suppress = Throwable.class)
        private static void onBeforeProcess(@Advice.Argument(value = 0) Exchange original) {
            if (tracer == null || original == null) {
                return;
            }
            System.out.println("Exchange identified!");
        }
    }
}
