package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import javafx.scene.text.Text;

import javafx.stage.Stage;


import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Paths;

import java.util.Optional;

import static java.lang.Integer.parseInt;


public class DatabaseController implements Serializable   {
    @FXML private AnchorPane anchorPane;
    @FXML private AnchorPane apBottomTable;
    @FXML private AnchorPane apEntryMenu;
    @FXML private AnchorPane apPwdGenerate;
    @FXML private AnchorPane entryPane;
    @FXML private Button btnCreate;
    @FXML private Button btnEnterMenu;
    @FXML private Button btnEditOK;
    @FXML private Button btnReturn;
    @FXML private Button btnLockDB;
    @FXML private Button btnPwdGenerator;
    @FXML private Button toggleButton;
    @FXML private Button togBtnPwd;
    @FXML private ContextMenu ctxTableMenu;
    @FXML private ImageView imgNotVisible;
    @FXML private ImageView imgPwdVisible;
    @FXML private ImageView imgVisible;
    @FXML private ImageView imgPwdNotVisible;
    @FXML private Hyperlink hyperLinkURL;
    @FXML private TableView<Entry> entryTable;
    @FXML private TableColumn<Entry, String> colTitel;
    @FXML private TableColumn<Entry, String> colUsername;
    @FXML private TableColumn<Entry, String> colURL;
    @FXML private TableColumn<Entry, String> colNotes;
    @FXML private TextField tfSearch;
    @FXML private TextField tfTitel;
    @FXML private TextField tfUsername;
    @FXML private TextField tfURL;
    @FXML private PasswordField pfPwdField;
    @FXML private TextArea tANotes;
    @FXML private TextField generatedPWDfield;
    @FXML private TextField tfPwd;
    @FXML private Text textUsername;
    @FXML private Text textPassword;
    @FXML private Text textNotes;
    @FXML private Text textTitel;
    @FXML private Text textGenePwdQuality;
    @FXML private Text textBruteForceTime;
    @FXML private Text textEntropy;

    static int pwdLength;
  //  private static final long serialVersionUID = 6529685098267757690L;
    private ObservableList<Entry> entryList = FXCollections.observableArrayList();
    VisibilityHandler visibilityHandler = new VisibilityHandler ();
    Slider sliderPwdGenerator = new Slider(4, 999, 1);
    Spinner<Integer> spinnerPwdGenerator;
    @FXML
    void generateChallengeResponse(ActionEvent event) throws Exception {
        HardwareKeyCmd.cmdGenerateCR();
        Thread.sleep(1900);
        save();
    }

    @FXML
    void configureChallengeResponse(ActionEvent event) throws Exception {
        HardwareKeyCmd.cmdConfigureCR();
        Thread.sleep(1900);
        save();
    }

    @FXML
    void copyPwd(ActionEvent event) throws Exception
    { final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            content.putString(selectedItem.getPassword());
            clipboard.setContent(content);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Password succesfully copied!");
        alert.showAndWait();
    }

    @FXML
    void copyGeneratedPWD(ActionEvent event) throws Exception
    {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(generatedPWDfield.getText());
        clipboard.setContent(content);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Password succesfully copied!");
        alert.showAndWait();
    }

    @FXML
    void copyUsername(ActionEvent event) throws Exception
    {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        Entry  selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            content.putString(selectedItem.getUsername());
            clipboard.setContent(content);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Username succesfully copied!");
        alert.showAndWait();
    }

    @FXML
        void createEntry (ActionEvent event) throws Exception {
            entryList.add(new Entry(tfTitel.getText(), tfUsername.getText(), tfURL.getText(), pfPwdField.getText(), tANotes.getText()));
            VisibilityHandler.showTableView(entryPane, apEntryMenu,tfSearch,entryTable, btnEditOK,apBottomTable);
            tfTitel.setText("");
            tfUsername.setText("");
            tfURL.setText("");
            pfPwdField.setText("");
            tANotes.setText("");
            save();

        }

    @FXML void editEntry (ActionEvent event) throws  Exception {
        Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {

            VisibilityHandler.entrySpecs (  apEntryMenu,tfSearch,btnCreate,entryTable,apBottomTable);
            tfTitel.setText(selectedItem.getTitle());
            tfUsername.setText(selectedItem.getUsername());
            tfURL.setText(selectedItem.getUrl());
            pfPwdField.setText(selectedItem.getPassword());
            tANotes.setText(selectedItem.getNotes());
            //the values inside the fields from the selected row is set to be the values stored inside the EntryTable
            // otherwise the values in the fieds from the selected entry would be empty
            VisibilityHandler.editEntryVisibility (btnEditOK,btnCreate);

            btnEditOK.setOnAction(e -> {
                try{
                    entryList.set(entryList.indexOf(selectedItem),selectedItem);
                    selectedItem.setTitle( tfTitel.getText());
                    selectedItem.setUsername( tfUsername.getText());
                    selectedItem.setURL( tfURL.getText());
                    selectedItem.setPassword( pfPwdField.getText());
                    selectedItem.setNotes(tANotes.getText());
                    // updates the value of both the tableview at the top and bottom of the page,
                    // with the newly added values, after clicking the OK button
                    VisibilityHandler.showTableView(entryPane, apEntryMenu,tfSearch,entryTable, btnEditOK,apBottomTable);
                    save();

                } catch (Exception E) {

                }
            });

        }
    }

    @FXML
    void deleteDatabase (ActionEvent event) throws Exception
    {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Warning, this will permanently delete all your passwords for this database");
        alert.setContentText("Are you sure you want to proceed?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            entryList.remove(entryList);
            File deleteFile = new File(FilePath.getPasswordFilePath()).getAbsoluteFile().getParentFile();
            FileHandler fileHandler= new FileHandler();
            fileHandler.deleteDir(deleteFile);
            Serialization.recentFilesSerialize(FilePath.getRecentFilesDir(), Paths.get(FilePath.getRecentFileDir()));
            SceneHandler.stageFullScreen(btnLockDB);
        }

    }
    @FXML
    void deleteEntry(ActionEvent event) throws  Exception {
        Entry  selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            entryList.remove(selectedItem);
            save();
        }

    }

    @FXML
    void entryMenu(ActionEvent event) throws Exception
    {
        VisibilityHandler.entrySpecs (  apEntryMenu,tfSearch,btnCreate,entryTable,apBottomTable);
    }

    @FXML
    void generatePwd (ActionEvent event) throws Exception {
        VisibilityHandler cVBH = new VisibilityHandler();
        cVBH.showPwdGenerateMenu(apPwdGenerate,entryPane,btnEditOK,apBottomTable);
        spinnerPwdGenerator();
        sliderPwdGenerator();
        btnPwdGenerator();
        textFieldPwdGenerator();
        apPwdGenerate.getChildren().addAll(spinnerPwdGenerator, sliderPwdGenerator);
        Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            entryList.set(entryList.indexOf(selectedItem), selectedItem);
            generatedPWDfield.setText(selectedItem.getPassword());
            PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());
            entryTable.getSelectionModel().clearSelection();
        }
        else {
            generatedPWDfield.setText(PasswordGenerator.generatePassword(spinnerPwdGenerator.getValue()));
            PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());
        }

    }

    @FXML void menuRandomPwd (ActionEvent event)
    {
        pfPwdField.setText(PasswordGenerator.generatePassword(32));
    }

    @FXML
    void newDB(ActionEvent event) throws  Exception {
        SceneHandler sceneHandler = new SceneHandler();
        sceneHandler.newDBdialog(btnReturn);
    }


    @FXML
    void openDB(ActionEvent event) throws Exception {

      SceneHandler sceneHandler = new SceneHandler();
       if (!sceneHandler.openDB()==true)
        {
            return;
        }
        String pwdFPNewValue= FilePath.getPasswordFilePath();
        String directoryNewValue =  FilePath.getSelectedDirectoryPath();
       //the new values of passwordFilePath and selectedDirectoryPath will be lost upon loading the FXML login "login.fxml"
        //so to keep the new values of both Strings,I create 2 new strings that store the values of the new paths,
        //load login.FXML, then set the values of the static path Strings to the new values.

           FXMLLoader fxmlLoader = new FXMLLoader();
           fxmlLoader.setLocation(getClass().getResource("login/login.fxml"));

           Scene scene = new Scene(fxmlLoader.load());
           Stage stage = new Stage();

           stage.setTitle("New Window");
           stage.setScene(scene);
           stage.show();

        FilePath.setPasswordFilePath( pwdFPNewValue);
        FilePath.setSelectedDirectoryPath(  directoryNewValue);
    }


    @FXML
    void returnTableView(ActionEvent event) throws Exception
    {
        VisibilityHandler.showTableView(entryPane, apEntryMenu,tfSearch,entryTable, btnEditOK,apBottomTable);
        apPwdGenerate.setDisable(true);
        apPwdGenerate.setVisible(false);
        apPwdGenerate.getChildren().remove(sliderPwdGenerator);
        // Empties the edited entry after clicking the cancel button,so the values
        // of the previously edited entry doesn't get transferred to the entry about to be created
        tfTitel.setText("");
        tfUsername.setText("");
        tfURL.setText("");
        pfPwdField.setText("");
        tANotes.setText("");
    }


    @FXML
    void lockDB(ActionEvent event) throws Exception {
        SceneHandler.stageFullScreen(btnLockDB);
    }

   void save () throws Exception {
    FileProtector fileProtector = new FileProtector();
    Secrets secrets = new Secrets();
    secrets.setEntry(entryList);
    secrets.setTimerSpecs(TimerSpecs.getTimerSpecs());
    fileProtector.encryption(entryList,secrets.getTimerSpecs());
    textTitel.setText(tfTitel.getText());
    textUsername.setText(tfUsername.getText());
    visibilityHandler.setSelectedPassword(pfPwdField.getText());
  // sets the password of the selected Entry to be the same as the newly added or edited password
    imgVisible.setVisible(false);
    imgNotVisible.setVisible(true);
    imgPwdNotVisible.setVisible(true);
    imgPwdVisible.setVisible(false);
    hyperLinkURL.setText(tfURL.getText());
    textNotes.setText(tANotes.getText());
    tfTitel.setText("");
    tfUsername.setText("");
    tfURL.setText("");
    pfPwdField.setText("");
    tANotes.setText("");
 }
    @FXML
    void saveEntry(ActionEvent event) throws Exception {

      save();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Database succesfully saved!");
        alert.showAndWait();
        
    }

    public void spinnerPwdGenerator() {
        spinnerPwdGenerator = (Spinner<Integer>) new Spinner(0, 999, 12);
        spinnerPwdGenerator.setPrefSize(75, 25);
        spinnerPwdGenerator.setLayoutX(580);
        spinnerPwdGenerator.setLayoutY(100);
        spinnerPwdGenerator.setEditable(true);
        spinnerPwdGenerator.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
        spinnerPwdGenerator.getEditor().setOnKeyReleased(e ->
            {
                generatedPWDfield.setText(PasswordGenerator.generatePassword(parseInt(newValue)));

                PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());
            });

            generatedPWDfield.setText(PasswordGenerator.generatePassword(parseInt(newValue)));

            PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy, generatedPWDfield.getText());
            sliderPwdGenerator.setValue(parseInt(newValue));

        });
    }

    public void sliderPwdGenerator()
    {
        sliderPwdGenerator.setBlockIncrement(1);
        sliderPwdGenerator.setValue(spinnerPwdGenerator.getValue());
        sliderPwdGenerator.setPrefWidth(570);
        sliderPwdGenerator.setLayoutY(110);
        sliderPwdGenerator.setShowTickLabels(true);
        sliderPwdGenerator.setOnMouseDragged(e -> {
          Double newData = sliderPwdGenerator.getValue();
          int value = newData.intValue();
          spinnerPwdGenerator.getValueFactory().setValue(value);
        });
    }
    public void btnPwdGenerator( ) {

        btnPwdGenerator.setOnAction(e ->
                // when the btnPwdGenerator button is pressed, the length of the integer pwdLenght is set to be equal to
                // the value of the pwdSpinner
                // then the string generated by generatePassword() is put inside the generatePwdField textfield
                //which is then used to calculate the the estimated cracking time of the password.
        {
            try {
                pwdLength = spinnerPwdGenerator.getValue();

                generatedPWDfield.setText(PasswordGenerator.generatePassword(pwdLength));

                PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());

            } catch (Exception E) {

            }

        });
    }
    void textFieldPwdGenerator() {
        generatedPWDfield.textProperty().addListener((observable, oldValue, newValue) -> {
          //  System.out.println("textfield changed from " + oldValue + " to " + newValue);
            PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  newValue);
        });
    }


    @FXML
    void updateMasterPwd(ActionEvent event) throws Exception {
        MasterPwdGui masterPwdGui = new MasterPwdGui();
        masterPwdGui.updateMasterPwd();
          save();
    }
@FXML
    void timerDialog(ActionEvent event) throws Exception {
   TimerHandler.timerDialog(entryList);
   TimerHandler.timerCountDown(btnLockDB,anchorPane);
    }

   static String hidePwd = "";
    @FXML
   private void initialize() throws Exception {
        entryTable.setPlaceholder(new Label("0 entries in the database. Click the + button to add new entries!"));
        anchorPane.setOnContextMenuRequested(e ->
                ctxTableMenu.show(anchorPane, e.getScreenX(), e.getScreenY()));

        String image = Main.class.getResource("authenticated/magnifying-glass.png").toExternalForm();
        tfSearch.setStyle("-fx-background-image: url('" + image + "'); " +
                " -fx-background-repeat: no-repeat; -fx-background-position: right; -fx-background-size: 38 24;");

        for (int i = 0; i < 12; i++) {
            hidePwd = '\u2022' + hidePwd;
            // Putting password string as 12 bullets, to hide the content and length of the user's passwords.
        }

        String finalHidePwd = hidePwd;
        entryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                textTitel.setText(selectedItem.getTitle());
                textNotes.setText(selectedItem.getNotes());
                textUsername.setText(selectedItem.getUsername());
                hyperLinkURL.setText(selectedItem.getUrl());
                hyperLinkURL.setOnAction(e ->
                {
                    try {
                        Desktop.getDesktop().browse(new URI("www." + selectedItem.getUrl()));
                        // opens the hyperlink in the user's main browser
                    } catch (Exception E) {

                    }

                });
                visibilityHandler = new VisibilityHandler ();
                visibilityHandler.pwdVisibilityTable(  toggleButton,   imgPwdVisible,  imgPwdNotVisible,   textPassword,
                        selectedItem.getPassword(),   finalHidePwd,entryTable);
                textPassword.setText(finalHidePwd);

            }
        });
        VisibilityHandler.pwdVisibilityMenu(togBtnPwd,imgVisible,imgNotVisible,tfPwd,pfPwdField);

            colTitel.setCellValueFactory(new PropertyValueFactory<>("title"));
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            colURL.setCellValueFactory(new PropertyValueFactory<>("url"));
            colNotes.setCellValueFactory(new PropertyValueFactory<>("Notes"));
            Authentication authentication = new Authentication();
            entryList.addAll(authentication.authenticated());
            TimerHandler.timerCountDown(btnLockDB,anchorPane);
            entryTable.setItems(entryList);
            searchFilter();
            btnEnterMenu.setOnAction(e -> {
                try {
                    VisibilityHandler.entrySpecs (  apEntryMenu,tfSearch,btnCreate,entryTable,apBottomTable);
                } catch (Exception E) {

                }
            });
           save();
           // Saves the database each time a user logs in,
        // so the challenge and response are never used more than once
    }

    @FXML
    private void searchFilter()
    {

        FilteredList<Entry> filteredData = new FilteredList<>(entryTable.getItems()
                , p -> true);

        tfSearch.textProperty().addListener((observable, oldValue,    newValue) -> {
            filteredData.setPredicate(entry -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare all columns  of every entry with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (entry.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches titel column.
                } else

                if (entry.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches Username
                } else

                if (entry.getUrl().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches URL.
                } else

                if (entry.getNotes().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches notes
                }
                return false; // Does not match any of the above.

            });
        });
        SortedList<Entry> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(entryTable.comparatorProperty());
        entryTable.setItems(sortedData);

    }

}

