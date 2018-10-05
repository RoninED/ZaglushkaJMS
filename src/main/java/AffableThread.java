//import com.ibm.mq.MQQueue;
import com.ibm.mq.jms.*;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.*;

import java.util.Enumeration;

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
        Listener.onMessage(new Message() {
            @Override
            public String getJMSMessageID() throws JMSException {
                return null;
            }

            @Override
            public void setJMSMessageID(String s) throws JMSException {

            }

            @Override
            public long getJMSTimestamp() throws JMSException {
                return 0;
            }

            @Override
            public void setJMSTimestamp(long l) throws JMSException {

            }

            @Override
            public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
                return new byte[0];
            }

            @Override
            public void setJMSCorrelationIDAsBytes(byte[] bytes) throws JMSException {

            }

            @Override
            public void setJMSCorrelationID(String s) throws JMSException {

            }

            @Override
            public String getJMSCorrelationID() throws JMSException {
                return null;
            }

            @Override
            public Destination getJMSReplyTo() throws JMSException {
                return null;
            }

            @Override
            public void setJMSReplyTo(Destination destination) throws JMSException {

            }

            @Override
            public Destination getJMSDestination() throws JMSException {
                return null;
            }

            @Override
            public void setJMSDestination(Destination destination) throws JMSException {

            }

            @Override
            public int getJMSDeliveryMode() throws JMSException {
                return 0;
            }

            @Override
            public void setJMSDeliveryMode(int i) throws JMSException {

            }

            @Override
            public boolean getJMSRedelivered() throws JMSException {
                return false;
            }

            @Override
            public void setJMSRedelivered(boolean b) throws JMSException {

            }

            @Override
            public String getJMSType() throws JMSException {
                return null;
            }

            @Override
            public void setJMSType(String s) throws JMSException {

            }

            @Override
            public long getJMSExpiration() throws JMSException {
                return 0;
            }

            @Override
            public void setJMSExpiration(long l) throws JMSException {

            }

            @Override
            public long getJMSDeliveryTime() throws JMSException {
                return 0;
            }

            @Override
            public void setJMSDeliveryTime(long l) throws JMSException {

            }

            @Override
            public int getJMSPriority() throws JMSException {
                return 0;
            }

            @Override
            public void setJMSPriority(int i) throws JMSException {

            }

            @Override
            public void clearProperties() throws JMSException {

            }

            @Override
            public boolean propertyExists(String s) throws JMSException {
                return false;
            }

            @Override
            public boolean getBooleanProperty(String s) throws JMSException {
                return false;
            }

            @Override
            public byte getByteProperty(String s) throws JMSException {
                return 0;
            }

            @Override
            public short getShortProperty(String s) throws JMSException {
                return 0;
            }

            @Override
            public int getIntProperty(String s) throws JMSException {
                return 0;
            }

            @Override
            public long getLongProperty(String s) throws JMSException {
                return 0;
            }

            @Override
            public float getFloatProperty(String s) throws JMSException {
                return 0;
            }

            @Override
            public double getDoubleProperty(String s) throws JMSException {
                return 0;
            }

            @Override
            public String getStringProperty(String s) throws JMSException {
                return null;
            }

            @Override
            public Object getObjectProperty(String s) throws JMSException {
                return null;
            }

            @Override
            public Enumeration getPropertyNames() throws JMSException {
                return null;
            }

            @Override
            public void setBooleanProperty(String s, boolean b) throws JMSException {

            }

            @Override
            public void setByteProperty(String s, byte b) throws JMSException {

            }

            @Override
            public void setShortProperty(String s, short i) throws JMSException {

            }

            @Override
            public void setIntProperty(String s, int i) throws JMSException {

            }

            @Override
            public void setLongProperty(String s, long l) throws JMSException {

            }

            @Override
            public void setFloatProperty(String s, float v) throws JMSException {

            }

            @Override
            public void setDoubleProperty(String s, double v) throws JMSException {

            }

            @Override
            public void setStringProperty(String s, String s1) throws JMSException {

            }

            @Override
            public void setObjectProperty(String s, Object o) throws JMSException {

            }

            @Override
            public void acknowledge() throws JMSException {

            }

            @Override
            public void clearBody() throws JMSException {

            }

            @Override
            public <T> T getBody(Class<T> aClass) throws JMSException {
                return null;
            }

            @Override
            public boolean isBodyAssignableTo(Class aClass) throws JMSException {
                return false;
            }
        });
    }
}
