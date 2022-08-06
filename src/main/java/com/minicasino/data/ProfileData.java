package com.minicasino.data;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileData {
    private static final ProfileData PROFILE_DATA_INSTANCE = new ProfileData();
    private final List<Profile> profileList;

    private ProfileData() {
        profileList = new ArrayList<>();
    }

    public static ProfileData getProfileDataInstance() {
        return PROFILE_DATA_INSTANCE;
    }

    public void addProfile(Profile profile) {
        if ((profile != null) && (profileList.size() < 5)) {
            profileList.add(profile);
        }
    }

    private void clearProfile(int index) {
        if (profileList.get(index) != null) {
            profileList.remove(index);
        }
    }

    public void saveProfileData() {
        //Root Element
        Element root = new Element("PROFILES");
        Document document = new Document();

        for (int i = 0; i < 5; i++) {
            Element profile = new Element("PROFILE" + i);
            profile.addContent(new Element("NAME").addContent(profileList.get(i).name));
            profile.addContent(new Element("BALANCE").addContent(String.valueOf(profileList.get(i).balance)));
            profile.addContent(new Element("HIGHEST_WIN").addContent(String.valueOf(profileList.get(i).highestWin)));

            root.addContent(profile);
        }

        document.setRootElement(root);

        XMLOutputter out = new XMLOutputter();
        out.setFormat(Format.getPrettyFormat());
        try {
            out.output(document, new FileWriter(new File("src/main/resources/xml/", "profiles.xml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadProfileData() {

    }

    public static class Profile {
        private final String name;
        private double balance;
        private double highestWin;

        public Profile(String name) {
            this.name = name;
            this.balance = 5000.0;
            this.highestWin = 0.0;
        }

        public double getBalance() {
            return balance;
        }

        public String getName() {
            return name;
        }

        public void increaseBalance(double amount) {
            if (amount > 0) {
                balance += amount;
            } else {
                System.out.println("ProfileData.Profile.increaseBalance() -> Can't update balance");
            }
        }

        public void decreaseBalance(double amount) {
            if ((amount > 0) && (amount <= balance)) {
                balance -= amount;
            } else {
                System.out.println("ProfileData.Profile.decreaseBalance() -> Can't update balance");
            }
        }

        public double getHighestWin() {
            return highestWin;
        }

        public void setHighestWin(double highestWin) {
            if (highestWin > this.highestWin) {
                this.highestWin = highestWin;
            } else {
                System.out.println("ProfileData.Profile.setHighestWin() -> Can't set highestWin");
            }
        }
    }
}
