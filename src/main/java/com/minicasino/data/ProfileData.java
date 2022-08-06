package com.minicasino.data;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
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
    private Profile activeProfile;

    private ProfileData() {
//        profileList = new ArrayList<>();
        profileList = FXCollections.observableArrayList();
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
        if (profileList.get(index) != null && index < 5) {
            profileList.set(index, new Profile());
        }
    }

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void saveProfileData(List<Profile> list) {
        // validate list parameter (mainly for the purpose of regenerating lost/on-existent profiles.xml):
        // TODO: check if that validation bit is really required
        boolean isListValid = true;
        if (list != null && !list.isEmpty() && list.size() < 6) {
            for (int i = 0; i < 5; i++) {
                if (list.get(i) == null) {
                    isListValid = false;
                    break;
                }
            }
        }

        if (!isListValid) {
            System.out.println("ProfileData.saveProfileData() -> invalid list parameter");
            return;
        }

        // produces XML file using JDOM:
        Element root = new Element("PROFILES");
        Document document = new Document();

        for (int i = 0; i < 5; i++) {
            Element profile = new Element("PROFILE" + i);
            assert list != null;
            profile.addContent(new Element("NAME").addContent(list.get(i).name));
            profile.addContent(new Element("BALANCE").addContent(String.valueOf(list.get(i).balance)));
            profile.addContent(new Element("HIGHEST_WIN").addContent(String.valueOf(list.get(i).highestWin)));
            profile.addContent(new Element("IS_EMPTY").addContent(String.valueOf(list.get(i).isEmpty)));

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

    // the method guarantees there's always an XML file with some data to load profileList from
    public void loadProfileData() {
        String profilesFilePath = "src/main/resources/xml/profiles.xml";
        // check if XML exists; otherwise generate one based on a list of empty profiles:
        File tempFile = new File(profilesFilePath);
        if (!tempFile.exists()) {
            List<Profile> tempProfileList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                tempProfileList.add(new Profile());
            }
            saveProfileData(tempProfileList);
        }

        // loads data from XML to ProfileData using JDOM:
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new File(profilesFilePath));
            Element root = document.getRootElement();

            for (int i = 0; i < 5; i++) {
                Element child = root.getChildren().get(i);
                Profile profile = new Profile(child.getChild("NAME").getText(),
                                              Double.parseDouble(child.getChild("BALANCE").getText()),
                                              Double.parseDouble(child.getChild("HIGHEST_WIN").getText()),
                                              Boolean.parseBoolean(child.getChild("IS_EMPTY").getText()));
                addProfile(profile);
            }
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
    }

    public static class Profile {
        private final String name;
        private double balance;
        private double highestWin;
        private final boolean isEmpty;

        // used when creating a new profile from the UI
        public Profile(String name) {
            while (true) {
                if (!name.equalsIgnoreCase("Empty")) {
                    this.name = name;
                    break;
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Warning!");
                            //alert.setHeaderText("placeholder");
                            alert.setContentText("The profile name cannot be set to: " + name);
                            alert.show();
                        }
                    });
                }
            }
            this.balance = 5000.0;
            this.highestWin = 0.0;
            this.isEmpty = false;
        }

        // used when loaded from XML
        public Profile(String name, double balance, double highestWin, boolean isEmpty) {
            this.name = name;
            this.balance = balance;
            this.highestWin = highestWin;
            this.isEmpty = isEmpty;
        }

        // used when generating a placeholder list for regenerated XML file (missing etc.)
        public Profile() {
            this.name = "Empty";
            this.balance = 5000.0;
            this.highestWin = 0.0;
            this.isEmpty = true;
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

        @Override
        public String toString() {
            return "Profile {" + name + ", " + balance + ", " + highestWin + "}";
        }
    }
}
