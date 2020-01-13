package main;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class Main {

    public static void main(String[] args) {
        String to = "send@gmail.com";
        String from = "to@gmail.com";
        Properties properties = System.getProperties();

        // properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        // properties.setProperty("mail.smtp.port", "465");
        // properties.setProperty("mail.smtp.socketFactory.port", "465");
        // properties.setProperty("mail.smtp.socketFactory.class",
        // "javax.net.ssl.SSLSocketFactory");
        // properties.setProperty("mail.smtp.auth", "true");
        properties.put("input.encoding", "utf-8");
        properties.setProperty("mail.smtp.host", "localhost");
        properties.setProperty("mail.smtp.port", "25");
        properties.setProperty("mail.smtp.socketFactory.port", "");
        properties.setProperty("mail.smtp.socketFactory.class", "");
        properties.setProperty("mail.smtp.auth", "false");

        Authenticator authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ahiahi@gmail.com", "enter_password");
            }
        };

        Session session = Session.getDefaultInstance(properties, authenticator);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // message.setSubject("Tiêu đề");
            message.setSubject("Tiêu đề", "UTF-8");
            // message.setContent("Nội dung email", "text/html");
            MimeBodyPart body = new MimeBodyPart();

            // velocity stuff.
            // Initialize velocity
            VelocityEngine ve = new VelocityEngine();
            ve.init();

            // get the template
            // Template t =
            // ve.getTemplate("src/main/resources/static/mail-body-template.vm");

            VelocityEngine engine = new VelocityEngine();
            engine.setProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
            engine.setProperty(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
            engine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH,
                    new File(Main.class.getClassLoader().getResource("mail").getFile()).getAbsolutePath());
            engine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS,
                    "org.apache.velocity.runtime.log.NullLogSystem");
            // engine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, ""); // khong
            // ghi
            // log
            // ra
            // file
            // velocity.log

            // 設定した値で初期化
            engine.init();
            // テンプレートの取得

            // get the template
            // Template t =
            // ve.getTemplate("src/main/resources/static/mail-body-template.vm");
            Template t = engine.getTemplate("mail-body-template.vm", "UTF-8");

            // RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
            // StringReader reader = new StringReader(strTemplate); // strTemplate: noi dung
            // trong file.vm
            // Template template = new Template();
            // template.setRuntimeServices(runtimeServices);

            // create context and add data
            VelocityContext context = new VelocityContext();
            context.put("fname", "Zubayer");
            context.put("lname", "Ahamed");
            context.put("proprietor", "coderslab.com");
            List<String> arr = new ArrayList<String>();
            arr.add("1");
            arr.add("2");
            arr.add("3");
            arr.add("4");
            context.put("arr", arr);

            /* now render the template into a StringWriter */
            StringWriter out = new StringWriter();
            // Writer out = null;
            t.merge(context, out);

            // velocity stuff end

            body.setContent(out.toString(), "text/html;charset=UTF-8");
            // body.setText(out.toString(), "UTF-8");
            // body.setText(out.toString(), "UTF-8", "text");
            // body.setText(out.toString(), "UTF-8", "html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(body);

            body = new MimeBodyPart();

            // String filename = "src/main/resources/static/mail-attachment-template.csv";
            // String filename = new File(Main.class.getClassLoader()
            // .getResource("mail" + File.separator +
            // "mail-attachment-template.csv").getFile()).getAbsolutePath();
            String filename = new File(
                    Main.class.getClassLoader().getResource("mail/mail-attachment-template.csv").getFile())
                            .getAbsolutePath();
            DataSource source = new FileDataSource(filename);
            body.setDataHandler(new DataHandler(source));
            body.setFileName("attachment.csv");
            multipart.addBodyPart(body);

            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}