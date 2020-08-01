import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LibMangJavaProj{
	public static void main(String[] args) {
		Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 2);
        int date=c.get(Calendar.DATE);
        try{
        	readFile("stdb.txt",date);
        }
        catch(IOException e){
        	System.out.println(e);
        }

	}

	public static void sendSms(String stName,String mobNo,String bookName,String date){
		 //Your authentication key
            String authkey = "";
            //Multiple mobiles numbers separated by comma
            String mobiles = mobNo;
            //Sender ID,While using route4 sender id should be 6 characters long.
            String senderId = "LIBMNG";
            //Your message to send, Add URL encoding here.
            String message = "Hello "+stName+"\nPlease renew your book "+bookName+" before "+date;
            //define route
            String route="4";
            String country="91";

            //Prepare Url
            URLConnection myURLConnection=null;
            URL myURL=null;
            BufferedReader reader=null;

            //encoding message
            String encoded_message=URLEncoder.encode(message);

            //Send SMS API
            String mainUrl="https://control.msg91.com/api/sendhttp.php?";

            //Prepare parameter string
            StringBuilder sbPostData= new StringBuilder(mainUrl);
            sbPostData.append("authkey="+authkey);
            sbPostData.append("&mobiles="+mobiles);
            sbPostData.append("&message="+encoded_message);
            sbPostData.append("&sender="+senderId);
            sbPostData.append("&route="+route);
            sbPostData.append("&country="+country);


            //final string
            mainUrl = sbPostData.toString();
            try
            {
                //prepare connection
                myURL = new URL(mainUrl);
                myURLConnection = myURL.openConnection();
                myURLConnection.connect();
                reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                //reading response
                String response;
                while ((response = reader.readLine()) != null)
                //print response
                System.out.println("Success Ref: "+response);
            	System.out.println("Name: "+stName);

                //finally close connection
                reader.close();
            }
            catch (IOException e)
            {
                    e.printStackTrace();
            }
        }

    	static void readFile(String fileName,int d) throws IOException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int date;
        String str;
        System.out.println("Sending Report!");
        while((line = br.readLine()) != null){
            StringTokenizer st=new StringTokenizer(line,"/;");
            while(st.hasMoreTokens()){
            	date= Integer.parseInt(st.nextToken());
				String mon=st.nextToken();
            	String year=st.nextToken();
            	String name=st.nextToken();
            	String mobNo=st.nextToken();
            	String bookName=st.nextToken();
            	if(date==d||(d-1==date)){
            		sendSms(name,mobNo,bookName,date+"/"+mon+"/"+year);
            	}

            }
        }
        br.close();
        fr.close();
        
    }
}
