package ecw.amossys.web.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

@RestController
public class MainController {

    private static final Logger logger = LogManager.getLogger("HelloWorld");

    @GetMapping("/api/")
    public String index() {
        return "Welcome to Smart Transport & Logistics API!";
    }
    
    @GetMapping(value="/api/swagger.json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public FileSystemResource getDoc() throws IOException{
        try{
            return new FileSystemResource(new File("./swagger.json"));
        }catch(Exception e){
            logger.error("Error in dl doc: "+e);
            return null;
        }
    }

    @GetMapping("/api/facility")
    public String admin(@RequestHeader("X-API-Key") String apikeyb64) {
        logger.info("Received a request on /api/facility");

        String apikey;

        // apikey: encode in base 64
        try{
            byte[] credDecoded = Base64.getDecoder().decode(apikeyb64.trim());
            apikey = new String(credDecoded);
        }
        catch(Exception e){
            return "You fail somethings...";
        }

        if ( apikey.equals(System.getenv("ACCESS_KEY")) ){
            String flag="";
            logger.info("Access granted");
            try{
                flag = new Scanner(new File("flag.txt")).useDelimiter("\\Z").next();
            }catch(Exception e){
                logger.info(e);
            }
            
            return "Here is you access code : "+flag;
        }
        
        return "Wrong ACCESS_KEY";
    }

    @PostMapping("/api/check")
    public String checkID(@RequestParam String prenom, @RequestParam String nom) {
        
        String p = prenom.trim().toLowerCase();
        String n = nom.trim().toLowerCase();

        Pattern pattern = Pattern.compile("jndi|ldap|rmi|dns|corba|:\\/\\/", Pattern.CASE_INSENSITIVE);
        boolean dectectAttack = pattern.matcher(prenom).find() | pattern.matcher(nom).find() ;
        if (dectectAttack){
            return "Attack detected! Try harder!";
        }

        // just check for fun
        if (n.equals("norris") && p.equals("chuck") ){
            return "As you are the great Chuck Norris in person, you will not need Log4Shell to break into the storage facility!";
        }
        else{ // Log with Log4j the identity, this is the vulnerable endpoint
            logger.info("This man tried to access : "+nom+" "+prenom);
        }
        // a clue on what vuln is needed to exploit
        return "Your are not in our database, we will Log your identity.";
    }

}