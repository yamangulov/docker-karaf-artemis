package ru.yamangulov.karafInput;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;
import java.io.File;

/**
 * Created by Andrey.Yamangulov
 * Date: 25.02.2021
 * Time: 13:22
 */
public class App {
    //system independent camel routes
    private static final String fileSeparator = System.getProperty("file.separator");
    private static final File[] roots = File.listRoots();
    private static final String inRoute = "file:" + roots[0] + "tmp" + fileSeparator + "in";
    private static final String outRoute = "file:" + roots[0] + "tmp" + fileSeparator + "out";
    public static void main(String[] args) throws Exception {
        CamelContext ctx = new DefaultCamelContext();
        ConnectionFactory cf = new ActiveMQConnectionFactory("user", "111111", "tcp://0.0.0.0:61616");
        ctx.addComponent("acitvemq", ActiveMQComponent.jmsComponentAutoAcknowledge(cf));
        ctx.addRoutes(
                new RouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        from(inRoute).to("acitvemq:queue:FileTransferQueue");
                    }
                }
        );
        ctx.addRoutes(
                new RouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        from("acitvemq:queue:FileTransferQueue").to(outRoute);
                    }
                }
        );
        while (true) {
            ctx.start();
        }
    }
}
