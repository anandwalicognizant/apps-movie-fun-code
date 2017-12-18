package org.superbiz.moviefun;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.superbiz.moviefun.albums.AlbumsUpdateMessageConsumer;

/**
 * Created by aw169 on 12/18/17.
 */
@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.uri}") String rabbitMqUri;
    @Value("${rabbitmq.queue}") String rabbitMqQueue;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(rabbitMqUri);
        return factory;
    }

    @Bean
    public IntegrationFlow amqpInbound(ConnectionFactory connectionFactory, AlbumsUpdateMessageConsumer consumer) {
        return IntegrationFlows
            .from(Amqp.inboundAdapter(connectionFactory, rabbitMqQueue))
            .handle(consumer::consume)
            .get();
    }

}
