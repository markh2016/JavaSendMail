


/* Auther Mark David Harrington 
   Thursday 20:27
   14th February 2019
*/



import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class JavaSendMail {


    private final Properties props = new Properties() ;
    private String from , to , subject , body ;
    private InternetAddress fromAddress = null;
    private InternetAddress toAddress = null;
    private File file ;
    private InputStream is , propertiesStream;
    
    private ClassLoader cls ;
       public JavaSendMail() throws IOException
       {
        
            
                    cls = getClass().getClassLoader();
                    propertiesStream = cls.getResourceAsStream("mailproperties/mail.properties");
                    
                    
                    if (propertiesStream != null) {
                            
                            // load all we need from the properties file 
                            props.load(propertiesStream);
                            from = props.getProperty("testmail.from");
                            to = props.getProperty("testmail.to");
                            subject = props.getProperty("testmail.subject");
                            body = props.getProperty("testmail.body");
                                
                            } else {
                                  System.out.println("Unable to read properties file") ;              // Properties file not found!
                            }
                            
            
                  
       } // EMD CONSTRUCTOR
       
      
        
       private boolean attachFile()
       {
         
        String userDir = System.getProperty("user.dir");
        
        System.out.println(userDir); 
        if(Files.exists(Paths.get(userDir+"/libbycomp.png"))) { 
                System.out.println("File libbycomp.png exits");
                file = new File (userDir+"/libbycomp.png") ;
                return true  ;
        }
        
        return false ;
       }
       
       
    
        private Properties getProperties()
        {
            return props ;
        }
       
        
	public  void send(final Properties props) throws AddressException{
		
    	        
		
                ArrayList email= new ArrayList();
                
                // add your dedicated email recipients   below 
                email.add(props.getProperty("list.recip1")) ;
                email.add(props.getProperty("list.recip2")) ;
                
                
                InternetAddress[] myList= new InternetAddress[email.size()];
                for (int i = 0; i < email.size(); i++) {
                myList[i] = new InternetAddress((String) email.get(i));
                }
                
                
                
                // start your default session
                
		Session mailSession = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(
                             props.getProperty("mail.user"), 
                             props.getProperty("mail.password"));
		}
		});
                
                // create a new message option 
                
		Message message = new MimeMessage(mailSession);
		
		
		try {
                        // we got this from your properties file
			fromAddress = new InternetAddress(from); 
			toAddress = new InternetAddress(to);
		} catch (AddressException e) {
		}
		
		try {
            
            
             // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            /* Set To: header field of the header. depending on what you want to do r 
            message.setRecipients(Message.RecipientType.TO,
               InternetAddress.parse(to)); */

            message.addRecipients(RecipientType.TO, myList);
            // Set Subject: header field
            message.setSubject("Testing Message 2 Mari Only");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("Hi Mari test message only If you could reply brilliant not a problem if you cant Missing Persons \n\n  " +
            "Libby has been missing for 14 days \n\n" + " The 21-year-old has been found since she went missing in Hull "
            +"Libby was last seen wearing an outfit similar to the one pictured - A black leather jacket, denim skirt and Vans \"Old Skool\" trainers \n\n"
            +"These are photo's of what she was last wearing https://ichef.bbci.co.uk/news/624/cpsprodpb/56F8/production/_105646222_libbyclothes.jpg  \n\n" 
            +"Last sighting of the University of Hull student was on Beverley Road, close to where she lived, at around 00.05 GMT on 1 February."
            +"Pawel Relowicz, 24, who was arrested on suspicion of abduction remains a person of interest, police have said." ) ;
            
            
            

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

         // Set text message part
            multipart.addBodyPart(messageBodyPart);

                        // Part two is attachment
                        
                        if(this.attachFile()==true) {
                        messageBodyPart = new MimeBodyPart();
                       
                            String filename = file.getAbsolutePath() ;
                            
                            DataSource source = new FileDataSource(file);
                            messageBodyPart.setDataHandler(new DataHandler(source));
                            messageBodyPart.setFileName(filename);
                            multipart.addBodyPart(messageBodyPart);
                        }
                        else 
                        {
                            System.out.println("No attachment  is avaialable or file does not exist");
                        
                        }
                        
                       
                        

                        // Send the complete message parts
                        message.setContent(multipart);



                                    // finally send you message    
                       Transport.send(message);			
		} catch (MessagingException e) {
			System.err.println("Error sending mail");
		}		
	}
	
	public static void main(String[] args) throws IOException, AddressException {
		
		
		JavaSendMail sendmail= new JavaSendMail();	
                sendmail.send(sendmail.getProperties());
	}
}