package ru.yamangulov.karafInput;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

/**
 * Created by Andrey.Yamangulov
 * Date: 25.02.2021
 * Time: 13:22
 */
public class App {
    public static void main(String[] args) throws Exception {
        CamelContext ctx = new DefaultCamelContext();
        ConnectionFactory cf = new ActiveMQConnectionFactory("user", "111111", "tcp://0.0.0.0:61616");
        ctx.addComponent("acitvemq", ActiveMQComponent.jmsComponentAutoAcknowledge(cf));
        ctx.addRoutes(
                new RouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        from("file:/tmp/in").to("acitvemq:queue:FileTransferQueue");
                    }
                }
        );
        ctx.addRoutes(
                new RouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        from("acitvemq:queue:FileTransferQueue").to("file:/tmp/out");
                    }
                }
        );
        while (true) {
            ctx.start();
        }
    }
}
