package gq.cstu.ccs.bankmsg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CSV {
    public class Guest {
        public String name;
        public boolean gender;
        public String phone;
        public String birthday;
    }
    public class UtilStrClass {
        public String peopleCount = "The count of the people:";
        public String name = "Guest name";
        public String gender = "Is his/her female?";
        public String phone = "Phone number";
        public String birthday = "Birthday";
    }
    private UtilStrClass UtilStr;
    public Guest[] guests;

    private String GenderToStringConverter(boolean genderBoolean) {
        return genderBoolean ? "Female" : "Male";
    }
    private boolean StringToGenderConverter(String genderString) {
        return (genderString == "Male") ? false : true;
    }
    private String DateToStringConverter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (month.length() == 1) month = "0" + month;
        if (day.length() == 1) day = "0" + day;
        return month + day;
    }

    public String Read(String filePath) {
        guests = ReadRaw(filePath);
        String result = new String();
        for (int t = 0; t < guests.length; t++) {
            result += "Name: " + guests[t].name + " | Gender: " + GenderToStringConverter(guests[t].gender) + " | Phone: " + guests[t].phone + " | Birthday: " + guests[t].birthday + "\n";
        }
        return result;
    }

    public Guest[] ReadRaw(String filePath)
    {
        Path fp = Paths.get(filePath);
        List<String> strA = new ArrayList();
        try {
            strA = Files.readAllLines(fp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(strA.get(0));
        int pN = Integer.valueOf(strA.get(0).split(",")[1]);
        Guest[] result = new Guest[pN];
        for (int lineNumber = 2; lineNumber < pN + 2; lineNumber++) {
            String[] tmp = strA.get(lineNumber).split(",");
            result[lineNumber - 2].name = tmp[0];
            result[lineNumber - 2].gender = Boolean.valueOf(tmp[1]);
            result[lineNumber - 2].phone = String.valueOf(tmp[2]);
            result[lineNumber - 2].birthday = String.valueOf(tmp[3]);
        }
        return result;
    }

    public void Write(String filePath, String context) {
        String[] strA = context.split("\n");
        int pN = strA.length;
        guests = new Guest[pN - 1];
        String[] tmp;
        for (int lC = 0; lC < pN - 1; lC++) {
            strA[lC] = strA[lC].replace("Name", "").replace("Gender", "").replace("Phone","").replace("Birthday","").replace(":","").replace(" ","").replace(",","");
            tmp = strA[lC].split("|");
            guests[lC].name = tmp[0];
            guests[lC].gender = StringToGenderConverter(tmp[1]);
            guests[lC].phone = tmp[2];
            guests[lC].birthday = tmp[3];
        }
        WriteRaw(filePath);
    }

    private void WriteRaw(String filePath) {
        Path fp = Paths.get(filePath);
        int totalLines = guests.length + 1;
        List<String> result = new ArrayList<>();
        result.set(0, UtilStr.peopleCount + "," + String.valueOf(totalLines - 1));
        result.set(1, UtilStr.name + "," + UtilStr.gender + "," + UtilStr.phone + "," + UtilStr.birthday);
        for (int pCount = 2; pCount < totalLines + 1; pCount++) {
            result.set(pCount, guests[pCount - 2].name + "," + guests[pCount - 2].gender + "," + guests[pCount - 2].phone + "," + guests[pCount - 2].birthday);
        }
        try {
            Files.delete(fp);
            Files.write(fp, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CreateNew(String filePath) {
        Path fp = Paths.get(filePath);
        List<String> result = new ArrayList<>();
        result.set(0, UtilStr.peopleCount + ",1");
        result.set(1, UtilStr.name + "," + UtilStr.gender + "," + UtilStr.phone + "," + UtilStr.birthday);
        result.set(2, "CrystalComputerStudio,True,0,20180828");
        try {
            Files.write(fp, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
