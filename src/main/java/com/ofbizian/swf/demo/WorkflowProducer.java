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
import org.apache.camel.main.Main;

public class WorkflowProducer {

    public static String COMMON_OPTIONS =
        "accessKey=XXX"
        + "&secretKey=XXX"
        + "&domainName=demo"
        + "&workflowList=demo-wlist"
        + "&activityList=demo-alist"
        + "&version=1.0"
        + "&clientConfiguration.endpoint=swf.eu-west-12.amazonaws.com";

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.enableHangupSupport();
        WorkflowProducerRoute route = new WorkflowProducerRoute();
        main.addRouteBuilder(route);
        main.run();
    }

    static class WorkflowProducerRoute extends RouteBuilder {

        @Override
        public void configure() throws Exception {

            from("timer://workflowProducer?repeatCount=10")
                    .setBody(property("CamelTimerCounter"))
                    .to("aws-swf://workflow?" + COMMON_OPTIONS + "&eventName=calculator")
                    .log("SENT WORKFLOW TASK ${body}");
        }
    }
}
