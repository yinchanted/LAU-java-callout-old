package com.swift.apigateway;
import com.apigee.flow.execution.ExecutionContext;
import com.apigee.flow.execution.ExecutionResult;
import com.apigee.flow.execution.spi.Execution;
import com.apigee.flow.message.MessageContext;
import com.apigee.flow.execution.Action;


import static java.util.Arrays.asList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
/**
 *
 * @author Deniss
 */
class LAUCalculator {
    
    private static final String CRLF = "\r\n";
    private static final String LAU_VERSION = "1.0";
    
    private static LAUCalculator LAU_CALCULATOR=null;
    private LAUCalculator() {
    }

    public static LAUCalculator getInstance() {
        if(LAU_CALCULATOR == null) {
            LAU_CALCULATOR = new LAUCalculator();
        }
        return LAU_CALCULATOR;
    }
     
    public String calculateLAU(String LAUApplicationID, String LAUCallTime,
            String LAURequestNonce, String LAUSigned, String LAUKey, String absPath,
            String requestBody) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keyspec = new SecretKeySpec(
                    LAUKey.getBytes(Charset.forName("US-ASCII")), "HmacSHA256");
            mac.init(keyspec);
            StringBuilder sb = new StringBuilder();
            sb.append("LAUApplicationID:").append(LAUApplicationID.trim()).append(CRLF);
            sb.append("LAUCallTime:").append(LAUCallTime.trim()).append(CRLF);
            sb.append("LAURequestNonce:").append(LAURequestNonce.trim()).append(CRLF);
            sb.append("LAUSigned:").append(LAUSigned.trim()).append(CRLF);
            sb.append("LAUVersion:").append(LAU_VERSION.trim()).append(CRLF);
            sb.append(absPath.trim()).append(CRLF);
            sb.append(requestBody);
            byte[] lau = mac.doFinal(sb.toString().getBytes("UTF-8"));
            byte[] lau_to_encode = new byte[16];
            System.arraycopy(lau, 0, lau_to_encode, 0, 16);
            String LAU = DatatypeConverter.printBase64Binary(lau_to_encode);
            return LAU;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException ex) {
            Logger.getLogger("Tracker API").log(Level.SEVERE, null, ex);
	    throw new RuntimeException("Verify error:" + ex.getMessage());
        }
    }
    public boolean verifyLAUConnector(String LAUApplicationID, String LAUCallTime,
            String LAURequestNonce, String LAUSigned, String LAUSignature, String LAUKey, String absPath,
            String requestBody) {
        
        boolean result = false;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keyspec = new SecretKeySpec(
                    LAUKey.getBytes(Charset.forName("US-ASCII")), "HmacSHA256");
            mac.init(keyspec);
            StringBuilder sb = new StringBuilder();
            sb.append("LAUApplicationID:").append(LAUApplicationID.trim()).append(CRLF);
            sb.append("LAUCallTime:").append(LAUCallTime.trim()).append(CRLF);
            sb.append("LAURequestNonce:").append(LAURequestNonce.trim()).append(CRLF);
            sb.append("LAUSigned:").append(LAUSigned.trim()).append(CRLF);
            sb.append("LAUVersion:").append(LAU_VERSION.trim()).append(CRLF);
            sb.append(absPath.trim()).append(CRLF);
            sb.append(requestBody);
            byte[] lau = mac.doFinal(sb.toString().getBytes("UTF-8"));
            byte[] lau_to_encode = new byte[16];
            System.arraycopy(lau, 0, lau_to_encode, 0, 16);
            String LAU = DatatypeConverter.printBase64Binary(lau_to_encode);
            // Compare the signature received from gpi Connector with the one calculated
            result = LAUSignature.equals(LAU);
            return result;
 
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException ex) {
            result = false;
            Logger.getLogger("Tracker API").log(Level.SEVERE, null, ex);
	    throw new RuntimeException("Verify error:" + ex.getMessage());
        }
    }
    
    public String calculateLAUConnector(String LAUApplicationID, String LAUCallTime,
            String LAURequestNonce, String LAUResponseNonce, String LAUVersion,
            String LAUKey, String responseBody) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keyspec = new SecretKeySpec(
                    LAUKey.getBytes(Charset.forName("US-ASCII")), "HmacSHA256");
            mac.init(keyspec);
            StringBuilder sb = new StringBuilder();
            sb.append("LAUApplicationID:").append(LAUApplicationID.trim()).append(CRLF);
            sb.append("LAUCallTime:").append(LAUCallTime.trim()).append(CRLF);
            sb.append("LAURequestNonce:").append(LAURequestNonce.trim()).append(CRLF);
            sb.append("LAUResponseNonce:").append(LAUResponseNonce.trim()).append(CRLF);
            sb.append("LAUVersion:").append(LAUVersion.trim()).append(CRLF);
            sb.append(responseBody);
            byte[] lau = mac.doFinal(sb.toString().getBytes("UTF-8"));
            byte[] lau_to_encode = new byte[16];
            System.arraycopy(lau, 0, lau_to_encode, 0, 16);
            String LAU = DatatypeConverter.printBase64Binary(lau_to_encode);
            return LAU;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException ex) {
            Logger.getLogger("Tracker API").log(Level.SEVERE, null, ex);
	    throw new RuntimeException("Verify error:" + ex.getMessage());
        }
    }
   
    public boolean verifyLAU(String LAUApplicationID, String LAUCallTime,
            String LAURequestNonce, String LAUResponseNonce, String LAUVersion,
            String LAUSignature, String LAUKey, String responseBody) {
        boolean result = false;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keyspec = new SecretKeySpec(
                    LAUKey.getBytes(Charset.forName("US-ASCII")), "HmacSHA256");
            mac.init(keyspec);
            StringBuilder sb = new StringBuilder();
            sb.append("LAUApplicationID:").append(LAUApplicationID.trim()).append(CRLF);
            sb.append("LAUCallTime:").append(LAUCallTime.trim()).append(CRLF);
            sb.append("LAURequestNonce:").append(LAURequestNonce.trim()).append(CRLF);
            sb.append("LAUResponseNonce:").append(LAUResponseNonce.trim()).append(CRLF);
            sb.append("LAUVersion:").append(LAUVersion.trim()).append(CRLF);
            sb.append(responseBody);
            byte[] lau = mac.doFinal(sb.toString().getBytes("UTF-8"));
            byte[] lau_to_encode = new byte[16];
            System.arraycopy(lau, 0, lau_to_encode, 0, 16);
            String LAU = DatatypeConverter.printBase64Binary(lau_to_encode);
            // Compare the signature received from gpi Connector with the one calculated
            result = LAUSignature.equals(LAU);
            return result;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException ex) {
            result = false;
            Logger.getLogger("Tracker API").log(Level.SEVERE, null, ex);
	    throw new RuntimeException("Verify error:" + ex.getMessage());
        }
    }
    
    
}

public class LauCallout implements Execution {
 
	static String LAUKey = "Abcd1234Abcd1234Abcd1234Abcd1234";

        private String ExtractFromReqHeader(MessageContext messageContext, String param, boolean required) throws RuntimeException {
                String resp = messageContext.getRequestMessage().getHeadersAsString(param);
	        if (required && resp == null) 
		        throw new RuntimeException("The LAU signature cannot be verified. Missing parameter-"+param);
	        return resp;
        }

        private String ExtractFromRespHeader(MessageContext messageContext, String param, boolean required) throws RuntimeException {
                String resp = messageContext.getRequestMessage().getHeadersAsString(param);
	        if (required && resp == null) 
		        throw new RuntimeException("The LAU signature cannot be verified. Missing parameter-"+param);
	        return resp;
        }


        private String ExtractFromReqHeader(MessageContext messageContext, String param) throws RuntimeException {
		return	ExtractFromReqHeader(messageContext, param, true);
        }

        private String ExtractFromRespHeader(MessageContext messageContext, String param) throws RuntimeException {
		return	ExtractFromRespHeader(messageContext, param, true);
        }

	public ExecutionResult execute(MessageContext messageContext, ExecutionContext executionContext) {
		
		try {
        String LAUApplicationID =  ExtractFromReqHeader(messageContext,"lauapplicationid");
        String LAUVersion =  ExtractFromReqHeader(messageContext,"lauversion");
        String LAUCallTime = ExtractFromReqHeader(messageContext,"laucalltime");
        String LAURequestNonce = ExtractFromReqHeader(messageContext,"laurequestnonce");
        String LAUSigned =  ExtractFromReqHeader(messageContext,"lausigned");
        String LAUSignature =  ExtractFromReqHeader(messageContext,"lausignature");
	messageContext.getMessage().removeHeader("Accept-Encoding");
	String absPath = messageContext.getVariable("proxy.basepath");
	absPath += messageContext.getVariable("proxy.pathsuffix");
        if (absPath == null) {
		throw new RuntimeException("The LAU signature cannot be verified. path is missing");
        }

	if (messageContext.getResponseMessage() == null) { // it is input flow verify result and get out
	   System.out.println("no response message");
	   String requestBody =  messageContext.getMessage().getContent();
           if (requestBody == null) {
		throw new RuntimeException("The LAU signature cannot be verified. Request Body is missing");
           }
           if (!LAUCalculator.getInstance().verifyLAUConnector(LAUApplicationID, LAUCallTime, LAURequestNonce, 
		LAUSigned, LAUSignature, LAUKey, absPath,requestBody)) 
           {
		throw new RuntimeException("The LAU signature cannot be verified. The digests do not match.");
	   }
            
            return ExecutionResult.SUCCESS;
        }
	// else:
	// This is rsponse flow compute signature and send reply
	System.out.println("handling response message");
        String LAUResponseNonce =  ExtractFromRespHeader(messageContext,"lauresponsenonce", false);
        if ( LAUResponseNonce == null) {
	   LAUResponseNonce = "593d24b3-01f9-4e84-89d5-2c88a5edb6c6";
	}
	messageContext.getMessage().removeHeader("lauapplicationid");
	messageContext.getMessage().removeHeader("lauversion");
	messageContext.getMessage().removeHeader("laucalltime");
	messageContext.getMessage().removeHeader("laurequestnonce");
	messageContext.getMessage().removeHeader("lausigned");
	messageContext.getMessage().removeHeader("lauresponsenonce");
	messageContext.getMessage().removeHeader("lausignature");
	String responseBody =  messageContext.getResponseMessage().getContent();
	if (responseBody ==null)
		responseBody="";
        LAUSignature = LAUCalculator.getInstance().calculateLAUConnector(LAUApplicationID, LAUCallTime,
 			LAURequestNonce, LAUResponseNonce, LAUVersion, LAUKey, 
			responseBody);

	messageContext.getMessage().setHeader("LAUApplicationID",LAUApplicationID);
	messageContext.getMessage().setHeader("LAUVersion",LAUVersion);
	messageContext.getMessage().setHeader("LAUCallTime",LAUCallTime);
	messageContext.getMessage().setHeader("LAURequestNonce",LAURequestNonce);
	//messageContext.getMessage().setHeader("LAUSigned",LAUSigned);
	messageContext.getMessage().setHeader("LAUResponseNonce",LAUResponseNonce);
	messageContext.getMessage().setHeader("LAUSignature",LAUSignature);
        return ExecutionResult.SUCCESS;
		 } catch (RuntimeException ex) {
            ExecutionResult executionResult = new ExecutionResult(false, Action.ABORT);
            //--Returns custom error message and header
            executionResult.setErrorResponse("{\"status\":{\"severity\":\"Logic\",\"code\":\"SwAPProxy003\",\"text\":\"" + ex.getMessage()+"\"}}");
            return executionResult;
	} catch (Exception ex) {
            ExecutionResult executionResult = new ExecutionResult(false, Action.ABORT);
            //--Returns custom error message and header
            executionResult.setErrorResponse("{\"status\":{\"severity\":\"Logic\",\"code\":\"SwAPProxy003\",\"text\":\"" + ex.getMessage()+"\"}}");
            return executionResult;
     }
   }
}
