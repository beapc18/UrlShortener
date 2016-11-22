package urlshortener.aeternum.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import urlshortener.common.repository.ClickRepository;
import urlshortener.common.repository.ShortURLRepository;
import urlshortener.common.web.UrlShortenerController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class QrGenerator {
    private String infoUrl, infoName, infoEmail, infoPhone,infoCompany,infoAdress;
    private String vCardText, urlVcard;

    private static final Logger LOG = LoggerFactory
        .getLogger(QrGenerator.class);

    @Autowired
    protected ShortURLRepository shortURLRepository;

    @Autowired
    protected ClickRepository clickRepository;

    @RequestMapping(value = "/qr", method = RequestMethod.GET)
    public ResponseEntity<String> generateQR(HttpServletRequest request) {
        Client client = ClientBuilder.newClient();
        String shortURL = request.getHeader("qrUrl");

        // VCard Starting Text
        vCardText = "BEGIN:VCARD\r\n" +
                    "VERSION:3.0\r\n";

        // Additions to Vcard
        if (!request.getHeader("qrUrl").equals("")){
            infoUrl = "URL:" + request.getHeader("qrUrl");
            vCardText += infoUrl;
        }
        if ((!request.getHeader("qrFname").equals("")) || (!request.getHeader("qrLname").equals(""))){
            infoName = "FN:" + request.getHeader("qrFname") + " " + request.getHeader("qrLname");
            vCardText += infoName;
        }
        if (!request.getHeader("qrEmail").equals("")){
            infoEmail = "EMAIL:" + request.getHeader("qrEmail");
            vCardText += infoEmail;
        }
        if (!request.getHeader("qrPhone").equals("")){
            infoPhone = "TEL:" + request.getHeader("qrPhone");
            vCardText += infoPhone;
        }
        if (!request.getHeader("qrCompany").equals("")){
            infoCompany =  "ORG:" + request.getHeader("qrCompany");
            vCardText += infoCompany;
        }
        if ((!request.getHeader("qrStreet").equals("")) || (!request.getHeader("qrZip").equals("")) || (!request.getHeader("qrCity").equals("")) || (!request.getHeader("qrCountry").equals(""))) {
            infoAdress = "ADR:" + request.getHeader("qrStreet") + ";" + request.getHeader("qrZip") + ";" + request.getHeader("qrCity") + ";" + request.getHeader("qrCountry");
            vCardText += infoAdress;
        }

        // Final text for Vcard
        vCardText += "REV:" + getCurrentTimeStamp() + "\r\n" + "END:VCARD";

        // Enconding of Vcard as an URL object
        try {
            urlVcard = URLEncoder.encode(vCardText, "UTF-8");
        }
        catch (UnsupportedEncodingException e){

        }

        Response response = client.target("https://chart.googleapis.com/chart?chs=500x500&cht=qr&chl="
            + urlVcard).request().get();

        if(response.getStatus() == 200){
            String qrCode = "\"https://chart.googleapis.com/chart?chs=500x500&cht=qr&chl=" + urlVcard + "\"";
            LOG.info("QR code generated");
            return new ResponseEntity<String>(qrCode, HttpStatus.CREATED);
        }else{
            LOG.info("Error to get the qr code");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Method that returns a time Stamp to generate the Vcard
    private static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
}
