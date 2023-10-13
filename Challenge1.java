import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Challenge1 {
    public static void main(String[] args) throws IOException {
        BufferedReader emailIDReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("What is the email id of the person you would like to look up?");
        String emailID = emailIDReader.readLine();
        emailIDReader.close();

        URL peopleURL = new URL("https://www.ecs.soton.ac.uk/people/" + emailID);
        getNameFromURL(peopleURL);
    }

    private static void getNameFromURL(URL url) throws IOException {
        BufferedReader readURL = new BufferedReader(new InputStreamReader(url.openStream()));

        String urlLine;
        while ((urlLine = readURL.readLine()) != null) {
            if (urlLine.contains("og:title")) {
                break;
            }
        }

        Pattern namePattern = Pattern.compile("\"(\\w|\\s)*\"", Pattern.CASE_INSENSITIVE);
        Matcher urlMatch = namePattern.matcher(urlLine);
        while (urlMatch.find()) {
            String dirtyName = urlMatch.group();
            String name = dirtyName.substring(1, dirtyName.length() - 2);
            System.out.println("Name: " + name);
        }
        readURL.close();
    }
}