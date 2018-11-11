package gq.cstu.ccs.bankmsg;

import android.content.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CSV {

    public static class Guest {
        public String name;
        public boolean gender;
        public String phone;
        public String birthday;
    }
    public static class UtilStrClass {
        public String peopleCount = "The count of the people:";
        public String name = "Guest name";
        public String gender = "Is his/her female?";
        public String phone = "Phone number";
        public String birthday = "Birthday";
    }
    public static UtilStrClass UtilStr = new UtilStrClass();
    public static Guest[] guests;

    public static String GenderToStringConverter(boolean genderBoolean) {
        return genderBoolean ? "Yes" : "No";
    }
    private static boolean StringToGenderConverter(String genderString) {
        return (genderString == "Yes");
    }
    private static String DateToStringConverter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (month.length() == 1) month = "0" + month;
        if (day.length() == 1) day = "0" + day;
        return month + day;
    }

    public static String Read(String filePath) throws IOException {
        guests = ReadRaw(filePath);
        String result = new String();
        for (int t = 0; t < guests.length; t++) {
            result += "Name: " + guests[t].name + " | Gender: " + GenderToStringConverter(guests[t].gender) + " | Phone: " + guests[t].phone + " | Birthday: " + guests[t].birthday + "\n";
        }
        return result;
    }

    public static Guest[] ReadRaw(String filePath) throws IOException {
        Path fp = Paths.get(filePath);
        List<String> strA = new ArrayList();
        strA = Files.readAllLines(fp);
        int pN = Integer.valueOf(strA.get(0).split(",")[1]);
        Guest[] result = new Guest[pN];
        //List<Guest> result = new ArrayList<>();
        for (int lineNumber = 2; lineNumber < pN + 2; lineNumber++) {
            String[] tmp = strA.get(lineNumber).split(",");
            /*
            result[lineNumber - 2].name = tmp[0];
            result[lineNumber - 2].gender = Boolean.valueOf(tmp[1]);
            result[lineNumber - 2].phone = String.valueOf(tmp[2]);
            result[lineNumber - 2].birthday = String.valueOf(tmp[3]);
            */
            /*
            Guest tmpG = new Guest();
            tmpG.name = tmp[0];
            tmpG.gender = Boolean.valueOf(tmp[1]);
            tmpG.phone = String.valueOf(tmp[2]);
            tmpG.birthday = String.valueOf(tmp[3]);
            result.set(lineNumber - 2, tmpG);
            */
            Guest tmpG = new Guest();
            tmpG.name = tmp[0];
            tmpG.gender = Boolean.valueOf(tmp[1]);
            tmpG.phone = String.valueOf(tmp[2]);
            tmpG.birthday = String.valueOf(tmp[3]);
            result[lineNumber - 2] = tmpG;
        }
        return result;
    }

    public static void Write(Context context, String filePath, String content) throws IOException {
        String[] strA = content.split("\n");
        int pN = strA.length;
        guests = new Guest[pN];
        Guest tmpG = new Guest();
        List<String> tmp = new ArrayList<>();
        for (int lC = 0; lC < pN; lC++) {
            //strA[lC] = strA[lC].replace("Name", "").replace("Gender", "").replace("Phone","").replace("Birthday","").replace(":","").replace(" ","").replace(",","");
            tmp = Arrays.asList(strA[lC].split("\\|BRK\\|"));
            tmpG.name = tmp.get(0);
            tmpG.gender = StringToGenderConverter(tmp.get(1));
            tmpG.phone = tmp.get(2);
            tmpG.birthday = tmp.get(3);
            guests[lC] = tmpG;
        }
        WriteRaw(context, filePath);
    }

    private static void WriteRaw(Context context, String filePath) throws IOException {
        Path fp = Paths.get(filePath);
        int totalLines = guests.length + 1;
        List<String> result = new ArrayList<>();
        //String result = new String();
        //result += UtilStr.peopleCount + "," + String.valueOf(totalLines - 1) + '\n';
        //result += UtilStr.name + "," + UtilStr.gender + "," + UtilStr.phone + "," + UtilStr.birthday + '\n';
        result.add(UtilStr.peopleCount + "," + String.valueOf(totalLines - 1));
        result.add(UtilStr.name + "," + UtilStr.gender + "," + UtilStr.phone + "," + UtilStr.birthday);
        for (int pCount = 2; pCount < totalLines + 1; pCount++) {
            //result += guests[pCount - 2].name + "," + guests[pCount - 2].gender + "," + guests[pCount - 2].phone + "," + guests[pCount - 2].birthday + '\n';
            result.add(guests[pCount - 2].name + "," + guests[pCount - 2].gender + "," + guests[pCount - 2].phone + "," + guests[pCount - 2].birthday);
        }
        Files.delete(fp);
        Files.createFile(fp);
        //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filePath, Context.MODE_PRIVATE));
        //outputStreamWriter.write(result);
        //outputStreamWriter.close();
        Files.write(fp, result);
    }

    public static void CreateNew(String filePath) throws IOException {
        Path fp = Paths.get(filePath);
        List<String> result = new ArrayList<>();
        result.add(UtilStr.peopleCount + ",1");
        result.add(UtilStr.name + "," + UtilStr.gender + "," + UtilStr.phone + "," + UtilStr.birthday);
        result.add("CrystalComputerStudio,True,0,0828");
        Files.createFile(fp);
        Files.write(fp, result);
    }
}
