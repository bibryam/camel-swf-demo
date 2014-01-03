package com.ofbizian.swf.demo;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.swf.SWFConstants;
import org.apache.camel.main.Main;

public class WorkflowConsumer {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.enableHangupSupport();
        WorkflowConsumerRoute route = new WorkflowConsumerRoute();
        main.addRouteBuilder(route);
        main.run();
    }

    static class WorkflowConsumerRoute extends RouteBuilder {

        @Override
        public void configure() throws Exception {

            from("aws-swf://workflow?" + WorkflowProducer.COMMON_OPTIONS + "&eventName=calculator")

                    .choice()
                        .when(header(SWFConstants.ACTION).isEqualTo(SWFConstants.SIGNAL_RECEIVED_ACTION))
                            .log("Signal received ${body}")

                        .when(header(SWFConstants.ACTION).isEqualTo(SWFConstants.GET_STATE_ACTION))
                            .log("State asked ${body}")

                        .when(header(SWFConstants.ACTION).isEqualTo(SWFConstants.EXECUTE_ACTION))

                            .setBody(simple("${body[0]}"))
                            .log("EXECUTION TASK RECEIVED ${body}")

                            .filter(simple("${body} > 5"))
                                .to("aws-swf://activity?" + WorkflowProducer.COMMON_OPTIONS + "&eventName=incrementor");
        }
    }

}
