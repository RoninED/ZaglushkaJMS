//import com.ibm.mq.MQQueue;
import com.ibm.mq.jms.*;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.*;

import static com.ibm.mq.jms.admin.APWMB.*;


class AffableThread extends Thread
{

    String host = "localhost";// хост, где расположен MQ-сервер
    int port = 1414; // порт для работы с MQ-сервером
    String mqQManager = "PraiseTheSun"; // менеджер очередей MQ
    String mqQChannel = "SYSTEM.DEF.SVRCONN"; // Канал для подключения к MQ-серверу
    String mqQIn = "MQ.Incoming"; // Очередь входящих сообщений
    String mqQOut = "MQ.Outgoing"; // Очередь исходящих сообщений

    MQQueueConnection mqConn;
    MQQueueConnectionFactory mqCF;
    MQQueueSession mqQSession;
    MQQueue mqIn;
    MQQueue mqOut;
    MQQueueSender mqSender;
    MQQueueReceiver mqReceiver;
    MessageProducer replyProd;

    public void sendAnswer (Message msg){
//            xmlAns – ответная xml
        try {
            TextMessage answer = mqQSession.createTextMessage("xmlAns");
            answer.setJMSCorrelationID(msg.getJMSMessageID());
            mqSender.send(answer);
            mqQSession.commit(); //Подтверждаем отправку сообщения
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()       //Этот метод будет выполнен в побочном потоке
     {
        try {
            mqCF = new MQQueueConnectionFactory();
            mqCF.setHostName(host);
            mqCF.setPort(port);
            mqCF.setQueueManager(mqQManager);
            mqCF.setChannel(mqQChannel);
            mqCF.setTransportType(WMQConstants.TIME_TO_LIVE_UNLIMITED);

            mqConn = (MQQueueConnection) mqCF.createQueueConnection();
            mqQSession = (MQQueueSession) mqConn.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
            mqIn = (MQQueue) mqQSession.createQueue("In"); // входная
            mqOut = (MQQueue) mqQSession.createQueue("Out"); // выходная
            mqSender = (MQQueueSender) mqQSession.createSender(mqOut);
            mqReceiver = (MQQueueReceiver) mqQSession.createReceiver(mqIn);
            this.replyProd = this.mqQSession.createProducer(null);
            this.replyProd.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            mqConn.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        javax.jms.MessageListener Listener = new javax.jms.MessageListener() {
            @Override
            public void onMessage(Message msg) {
                System.out.println(msg);
//                Здесь будет ваш код парсингаи обработки принятого сообщения.
                if (msg instanceof TextMessage) {

                    try {
                        TextMessage tMsg = (TextMessage) msg;
                        String msgText = ((TextMessage) msg).getText();
                        mqQSession.commit(); //Подтверждаем что мы прочитали это сообщение
                        sendAnswer(msg); //Отправляем ответное сообзение }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
